package io.beatmaps.kabt.external

import io.beatmaps.kabt.SeekOrigin
import io.beatmaps.kabt.base.IObject

expect object UFS {
    fun init()
    fun cleanup()

    fun mountArchive(path: String, mountPoint: String): Long
    fun unmountArchive(handle: Long)

    fun getArchiveNodeCount(handle: Long): Long
    fun getArchiveNode(handle: Long, nodeIndex: Int): ArchiveNodeData

    fun openFile(path: String): Long
    fun readFile(handle: Long, size: Long): UByteArray
    fun seekFile(handle: Long, offset: Long, origin: SeekOrigin): Long
    fun getFileSize(handle: Long): Long
    fun closeFile(handle: Long)

    fun openSerializedFile(path: String): Long
    fun closeSerializedFile(handle: Long)

    fun getExternalReferenceCount(handle: Long): Int
    fun getExternalReference(handle: Long, index: Int): ExternalReferenceData
    fun getObjectCount(handle: Long): Int
    fun getObjectInfo(handle: Long, len: Int): List<IObject>
    fun getTypeTree(handle: Long, objectId: Long): Long
    fun getRefTypeTypeTree(handle: Long, className: String, namespaceName: String, assemblyName: String): Long
    fun getTypeTreeNodeInfo(handle: Long, node: Int): TypeTreeNodeInfoData
}
