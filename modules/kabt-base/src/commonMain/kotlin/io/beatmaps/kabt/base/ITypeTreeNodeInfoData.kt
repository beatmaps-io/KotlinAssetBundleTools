package io.beatmaps.kabt.base

interface ITypeTreeNodeInfoData {
    val nodeType: String
    val nodeName: String
    val offset: Int
    val size: Int
    val flags: Byte
    val metaFlags: Int
    val firstChild: Int
    val nextNode: Int
}
