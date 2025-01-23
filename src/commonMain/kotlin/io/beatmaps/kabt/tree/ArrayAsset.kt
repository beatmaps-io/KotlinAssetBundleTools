package io.beatmaps.kabt.tree

import io.beatmaps.kabt.Offset
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.base.TypeTreeNodeBase

class ArrayAsset(reader: IUnityFileReader, node: TypeTreeNodeBase, offset: Offset) : ArrayAssetBase(reader, node, offset) {
    override val items: List<Asset> = List(arraySize) { create(reader, dataNode, offset) }

    override fun print(sb: StringBuilder, level: Int, index: Int) {
        super.print(sb, level, index)

        items.forEachIndexed { idx, asset ->
            asset.print(sb, level + 1, idx)
        }
    }
}
