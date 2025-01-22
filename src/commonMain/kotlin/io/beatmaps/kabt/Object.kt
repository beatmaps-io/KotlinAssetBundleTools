package io.beatmaps.kabt

import io.beatmaps.kabt.base.IObject
import io.beatmaps.kabt.base.ISerializedFile
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.base.TypeTreeNodeBase

data class Object(private val reader: IUnityFileReader, private val file: ISerializedFile, private val info: IObject) : IObject {
    override val id = info.id
    override val offset = info.offset
    override val size = info.size
    override val typeId = info.typeId

    fun dump(sb: StringBuilder, node: TypeTreeNodeBase, offset: Offset, level: Int, arrayIndex: Int = -1) {
        var skipChildren = false

        if (!node.isArray) {
            repeat(level * 2) { sb.append(' ') }

            if (level != 0) {
                sb.append(node.name)
                if (arrayIndex >= 0) {
                    sb.append('[')
                    sb.append(arrayIndex)
                    sb.append(']')
                }
                sb.append(" (")
                sb.append(node.type)
                sb.append(')')
            } else {
                sb.append(node.type)
            }

            if (node.isBasicType) {
                sb.append(' ')
                sb.append(reader.readValue(node, offset.offset))

                offset.plus(node.size)
            } else if (node.type == "string") {
                val stringSize = reader.readInt32(offset.offset)
                sb.append(' ')
                sb.append(reader.readString(offset.offset + 4, stringSize))
                offset.plus(stringSize + 4)
                skipChildren = true
            }

            println(sb.toString())
            sb.clear()

            if (node.isManagedReferenceRegistry) {
                dumpManagedReferenceRegistry(sb, node, offset, level + 1)
                skipChildren = true
            }
        } else {
            dumpArray(sb, node, offset, level)
            skipChildren = true
        }

        if (!skipChildren) {
            node.children.forEach { child ->
                dump(sb, child, offset, level + 1)
            }
        }

        if (node.alignBytes || node.childAlignBytes) offset.align()
    }

    private fun dumpArray(sb: StringBuilder, node: TypeTreeNodeBase, offset: Offset, level: Int) {
        val sizeNode = node.children[0]
        val dataNode = node.children[1]

        if (sizeNode.size != 4 || !sizeNode.isLeaf) throw Exception("Invalid array size")

        val arraySize = reader.readInt32(offset.offset)
        offset.plus(4)

        repeat(level * 2) { sb.append(' ') }
        sb.append("Array <")
        sb.append(dataNode.type)
        sb.append(">[")
        sb.append(arraySize)
        sb.append(']')

        println(sb.toString())
        sb.clear()

        if (arraySize > 0) {
            if (dataNode.isBasicType) {
                repeat((level + 1) * 2) { sb.append(' ') }

                if (arraySize > 256 && SKIP_LARGE_ARRAYS) {
                    sb.append("<Skipped>")
                    offset.plus(dataNode.size * arraySize)
                } else {
                    dataNode.kotlinType?.let { kt ->
                        val array = reader.readArray(kt, offset.offset, arraySize)
                        offset.plus(dataNode.size * arraySize)

                        sb.append(array.joinToString())
                    }
                }

                println(sb.toString())
                sb.clear()
            } else {
                repeat(arraySize) { idx ->
                    dump(sb, dataNode, offset, level + 1, idx)
                }
            }
        }
    }

    private fun dumpManagedReferenceRegistry(sb: StringBuilder, node: TypeTreeNodeBase, offset: Offset, level: Int) {
        if (node.children.size < 2) throw Exception("Invalid ManagedReferenceRegistry")

        val version = reader.readInt32(offset.offset)
        dump(sb, node.children[0], offset, level)

        var typeNode: TypeTreeNodeBase
        var objData: TypeTreeNodeBase

        if (version == 1) {
            val objNode = node.children[1]

            typeNode = node.children[0]
            objData = objNode.children[1]

            var i = 0L
            while (dumpManagedReferenceData(sb, reader, file, typeNode, objData, offset, level, i++)) {
                // Wait until returns false
            }
        } else if (version == 2) {
            val refIdsVector = node.children[1]

            if (refIdsVector.children.isEmpty() || refIdsVector.name != "RefIds")
                throw Exception("Invalid ManagedReferenceRegistry RefIds vector")

            val refIdsArray = node.children[0]

            if (refIdsArray.children.size != 2 || !refIdsArray.isArray)
                throw Exception("Invalid ManagedReferenceRegistry RefIds array")

            val arraySize = reader.readInt32(offset.offset)
            offset.plus(4)

            val refObj = refIdsArray.children[1]

            repeat(arraySize) {
                val rid = reader.readInt64(offset.offset)
                offset.plus(8)

                typeNode = refObj.children[1]
                objData = refObj.children[2]

                dumpManagedReferenceData(sb, reader, file, typeNode, objData, offset, level, rid)
            }
        } else {
            throw Exception("Invalid ManagedReferenceRegistry version")
        }
    }

    private fun dumpManagedReferenceData(sb: StringBuilder, reader: IUnityFileReader, file: ISerializedFile, refTypeNode: TypeTreeNodeBase, referencedTypeDataNode: TypeTreeNodeBase, offset: Offset, level: Int, id: Long): Boolean {
        if (refTypeNode.children.size < 3)
            throw Exception("Invalid ReferencedManagedType")

        repeat(level * 2) { sb.append(' ') }
        sb.append("rid(")
        sb.append(id)
        sb.append(") ReferencedObject")

        println(sb.toString())
        sb.clear()

        val newLevel = level + 1
        val refTypeOffset = offset.offset
        val stringSize = reader.readInt32(offset.offset)
        val className = reader.readString(offset.offset + 4, stringSize)
        offset.plus(stringSize + 4).align()

        val namespaceSize = reader.readInt32(offset.offset)
        val namespaceName = reader.readString(offset.offset + 4, namespaceSize)
        offset.plus(namespaceSize + 4).align()

        val assemblySize = reader.readInt32(offset.offset)
        val assemblyName = reader.readString(offset.offset + 4, assemblySize)
        offset.plus(assemblySize + 4).align()

        if (className == "Terminus" && namespaceName == "UnityEngine.DMAT" && assemblyName == "FAKE_ASM")
            return false

        dump(sb, refTypeNode, Offset(refTypeOffset), newLevel)

        repeat(newLevel * 2) { sb.append(' ') }
        sb.append(referencedTypeDataNode.name)
        sb.append(' ')
        sb.append(referencedTypeDataNode.type)
        sb.append(' ')

        println(sb.toString())
        sb.clear()

        if (id == -1L || id == -2L) {
            repeat(newLevel * 2) { sb.append(' ') }
            sb.append(if (id == -1L) "  unknown" else "  null")

            println(sb.toString())
            sb.clear()

            return true
        }

        val refTypeRoot: TypeTreeNodeBase = file.getRefTypeTypeTreeRoot(className, namespaceName, assemblyName)

        refTypeRoot.children.forEach { child ->
            dump(sb, child, offset, newLevel + 1)
        }

        return true
    }

    companion object {
        const val SKIP_LARGE_ARRAYS = true
    }
}
