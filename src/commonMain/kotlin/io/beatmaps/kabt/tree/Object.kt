package io.beatmaps.kabt.tree

import io.beatmaps.kabt.Offset
import io.beatmaps.kabt.base.IObject
import io.beatmaps.kabt.base.IUnityFileReader

data class Object(private val reader: IUnityFileReader, private val info: IObject) : IObject {
    override val id = info.id
    override val offset = info.offset
    override val size = info.size
    override val typeId = info.typeId

    val asset by lazy {
        val root = reader.serializedFile.getTypeTreeRoot(id)
        Asset.create(reader, root, Offset(offset))
    }

    fun dump(sb: StringBuilder) {
        asset.print(sb, 0)
    }
}
