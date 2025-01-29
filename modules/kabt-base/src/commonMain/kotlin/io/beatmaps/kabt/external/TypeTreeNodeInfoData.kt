package io.beatmaps.kabt.external

import io.beatmaps.kabt.base.ITypeTreeNodeInfoData

data class TypeTreeNodeInfoData(
    override val nodeType: String,
    override val nodeName: String,
    override val offset: Int,
    override val size: Int,
    override val flags: Byte,
    override val metaFlags: Int,
    override val firstChild: Int,
    override val nextNode: Int
) : ITypeTreeNodeInfoData {
    constructor() : this("", "", 0, 0, 0, 0, 0, 0)
}
