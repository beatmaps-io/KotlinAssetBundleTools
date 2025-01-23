package io.beatmaps.kabt.tree

import io.beatmaps.kabt.Offset
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.base.TypeTreeNodeBase

// TODO: Pull children out of print???
class ManagedReferenceRegistryAsset(private val reader: IUnityFileReader, private val node: TypeTreeNodeBase, private val offset: Offset) : Asset(node) {
    override fun print(sb: StringBuilder, level: Int, index: Int) {
        repeat(level * 2) { sb.append(' ') }

        if (level != 0) {
            sb.append(node.name)
            if (index >= 0) {
                sb.append('[')
                sb.append(index)
                sb.append(']')
            }
            sb.append(" (")
            sb.append(node.type)
            sb.append(')')
        } else {
            sb.append(node.type)
        }

        println(sb.toString())
        sb.clear()

        if (node.children.size < 2) throw Exception("Invalid ManagedReferenceRegistry")

        val version = reader.readInt32(offset.offset)
        ComplexAsset(reader, node.children[0], offset)
            .print(sb, level)

        var typeNode: TypeTreeNodeBase
        var objData: TypeTreeNodeBase

        if (version == 1) {
            val objNode = node.children[1]

            typeNode = node.children[0]
            objData = objNode.children[1]

            var i = 0L
            while (dumpManagedReferenceData(sb, reader, typeNode, objData, offset, level, i++)) {
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

                dumpManagedReferenceData(sb, reader, typeNode, objData, offset, level, rid)
            }
        } else {
            throw Exception("Invalid ManagedReferenceRegistry version")
        }
    }

    private fun dumpManagedReferenceData(sb: StringBuilder, reader: IUnityFileReader, refTypeNode: TypeTreeNodeBase, referencedTypeDataNode: TypeTreeNodeBase, offset: Offset, level: Int, id: Long): Boolean {
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

        ComplexAsset(reader, refTypeNode, Offset(refTypeOffset)).also {
            it.print(sb, newLevel)
        }

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

        val refTypeRoot: TypeTreeNodeBase = reader.serializedFile.getRefTypeTypeTreeRoot(className, namespaceName, assemblyName)

        refTypeRoot.children.forEach { child ->
            ComplexAsset(reader, child, offset).also {
                it.print(sb, newLevel + 1)
            }
        }

        return true
    }
}