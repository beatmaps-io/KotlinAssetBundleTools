package io.beatmaps.kabt.external

data class TypeTreeNodeInfoData(val nodeType: String, val nodeName: String, val offset: Int, val size: Int, val flags: Byte, val metaFlags: Int, val firstChild: Int, val nextNode: Int) {
    constructor() : this("", "", 0, 0, 0, 0, 0, 0)
}
