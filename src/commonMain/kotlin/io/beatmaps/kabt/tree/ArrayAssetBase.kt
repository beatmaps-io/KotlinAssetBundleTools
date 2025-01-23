package io.beatmaps.kabt.tree

import io.beatmaps.kabt.Offset
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.base.TypeTreeNodeBase

abstract class ArrayAssetBase(reader: IUnityFileReader, node: TypeTreeNodeBase, offset: Offset) : Asset(node) {
    protected val sizeNode = node.children[0]
    protected val dataNode = node.children[1]

    val arrayType = dataNode.type
    protected val arraySize: Int
    abstract val items: List<Any>

    init {
        if (sizeNode.size != 4 || !sizeNode.isLeaf) throw Exception("Invalid array size")

        arraySize = reader.readInt32(offset.offset)
        offset.plus(4)
    }

    override fun print(sb: StringBuilder, level: Int, index: Int) {
        sb.appendLine("${indent(level)}Array <$arrayType>[$arraySize]")
    }
}