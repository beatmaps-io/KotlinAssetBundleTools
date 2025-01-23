package io.beatmaps.kabt.tree

import io.beatmaps.kabt.Offset
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.base.TypeTreeNodeBase

class StringAsset(reader: IUnityFileReader, node: TypeTreeNodeBase, offset: Offset) : Asset(node) {
    val string: String

    init {
        val stringSize = reader.readInt32(offset.offset)
        string = reader.readString(offset.offset + 4, stringSize)
        offset.plus(stringSize + 4)
    }

    override fun print(sb: StringBuilder, level: Int, index: Int) {
        super.print(sb, level, index)

        sb.appendLine(" $string")
    }
}
