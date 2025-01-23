package io.beatmaps.kabt.tree

import io.beatmaps.kabt.Offset
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.base.TypeTreeNodeBase

sealed class Asset(private val node: TypeTreeNodeBase) {
    val name = node.name
    val type = node.type

    protected fun indent(level: Int) = " ".repeat(level * 2)

    open fun print(sb: StringBuilder, level: Int = 0, index: Int = -1) {
        sb.append(indent(level))

        if (level != 0) {
            sb.append("${node.name}${if (index >= 0) "[$index]" else ""} (${node.type})")
        } else {
            sb.append(node.type)
        }
    }

    companion object {
        private fun createInternal(reader: IUnityFileReader, node: TypeTreeNodeBase, offset: Offset) =
            if (node.isArray) {
                if (node.children[1].isBasicType) {
                    BasicArrayAsset(reader, node, offset)
                } else {
                    ArrayAsset(reader, node, offset)
                }
            } else if (node.isManagedReferenceRegistry) {
                ManagedReferenceRegistryAsset(reader, node, offset)
            } else if (node.type == "string") {
                StringAsset(reader, node, offset)
            } else if (node.type == "map") {
                MapAsset(reader, node, offset)
            } else if (node.type == "pair") {
                PairAsset(reader, node, offset)
            } else if (node.isBasicType) {
                BasicAsset(reader, node, offset)
            } else {
                ComplexAsset(reader, node, offset)
            }

        fun create(reader: IUnityFileReader, node: TypeTreeNodeBase, offset: Offset) =
            createInternal(reader, node, offset).also {
                if (node.alignBytes || node.childAlignBytes) offset.align()
            }
    }
}


