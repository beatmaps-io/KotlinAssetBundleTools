package io.beatmaps.kabt.external

import io.beatmaps.kabt.Handle
import io.beatmaps.kabt.SeekOrigin
import io.beatmaps.kabt.base.IArchiveNodeData
import io.beatmaps.kabt.base.IExternalReferenceData
import io.beatmaps.kabt.base.IObject
import io.beatmaps.kabt.base.ITypeTreeNodeInfoData

expect object UFS {
    fun init()
    fun cleanup()

    fun mountArchive(path: String, mountPoint: String): Handle
    fun unmountArchive(handle: Handle)

    fun getArchiveNodeCount(handle: Handle): Long
    fun getArchiveNode(handle: Handle, nodeIndex: Int): IArchiveNodeData

    fun openFile(path: String): Handle
    fun readFile(handle: Handle, size: Long): UByteArray
    fun seekFile(handle: Handle, offset: Long, origin: SeekOrigin): Long
    fun getFileSize(handle: Handle): Long
    fun closeFile(handle: Handle)

    fun openSerializedFile(path: String): Handle
    fun closeSerializedFile(handle: Handle)

    fun getExternalReferenceCount(handle: Handle): Int
    fun getExternalReference(handle: Handle, index: Int): IExternalReferenceData
    fun getObjectCount(handle: Handle): Int
    fun getObjectInfo(handle: Handle, len: Int): List<IObject>
    fun getTypeTree(handle: Handle, objectId: Long): Handle
    fun getRefTypeTypeTree(handle: Handle, className: String, namespaceName: String, assemblyName: String): Handle
    fun getTypeTreeNodeInfo(handle: Handle, node: Int): ITypeTreeNodeInfoData
}
