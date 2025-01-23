package io.beatmaps.kabt.tree

import io.beatmaps.kabt.Offset
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.base.TypeTreeNodeBase

class BasicAsset(reader: IUnityFileReader, node: TypeTreeNodeBase, offset: Offset) : Asset(node) {
    val localValue: Any

    init {
        localValue = reader.readValue(node, offset.offset)
        offset.plus(node.size)
    }

    override fun print(sb: StringBuilder, level: Int, index: Int) {
        super.print(sb, level, index)

        sb.appendLine(" $localValue")
    }
}
