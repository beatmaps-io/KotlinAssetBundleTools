package io.beatmaps.kabt.tree

import io.beatmaps.kabt.Offset
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.base.TypeTreeNodeBase

class BasicArrayAsset(reader: IUnityFileReader, node: TypeTreeNodeBase, offset: Offset) : ArrayAssetBase(reader, node, offset) {
    override val items: List<Any> = dataNode.kotlinType?.let { kt ->
        reader.readArray(kt, offset.offset, arraySize).also {
            offset.plus(dataNode.size * arraySize)
        }
    } ?: emptyList()

    override fun print(sb: StringBuilder, level: Int, index: Int) {
        super.print(sb, level, index)

        if (arraySize > 0) {
            sb.appendLine("${indent(level)}${items.joinToString()}")
        }
    }
}
