package io.beatmaps.kabt.tree

import io.beatmaps.kabt.Offset
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.base.TypeTreeNodeBase

class PairAsset(reader: IUnityFileReader, node: TypeTreeNodeBase, offset: Offset) : Asset(node) {
    private val children = node.children.map { child ->
        create(reader, child, offset)
    }

    val first = children[0]
    val second = children[1]

    override fun print(sb: StringBuilder, level: Int, index: Int) {
        super.print(sb, level, index)
        sb.appendLine()

        children.forEach { child ->
            child.print(sb, level + 1)
        }
    }
}
