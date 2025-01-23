package io.beatmaps.kabt.tree

import io.beatmaps.kabt.Offset
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.base.TypeTreeNodeBase

class ComplexAsset(reader: IUnityFileReader, node: TypeTreeNodeBase, offset: Offset) : Asset(node) {
    val children = node.children.map { child ->
        create(reader, child, offset)
    }

    override fun print(sb: StringBuilder, level: Int, index: Int) {
        super.print(sb, level, index)
        sb.appendLine()

        children.forEach { child ->
            child.print(sb, level + 1)
        }
    }
}
