package io.beatmaps.kabt

import io.beatmaps.kabt.base.IObject
import io.beatmaps.kabt.external.ArchiveNodeData
import io.beatmaps.kabt.external.ExternalReferenceData
import io.beatmaps.kabt.external.TypeTreeNodeInfoData

class UFSJNI {
    external fun init(): Int
    external fun cleanup(): Int
    external fun mountArchive(path: String, mountPoint: String, handle: JNIHandleL): Int
    external fun unmountArchive(handle: Long): Int
    external fun getArchiveNodeCount(handle: Long, count: JNIHandleL): Int
    external fun getArchiveNode(handle: Long, nodeIndex: Int, data: ArchiveNodeData): Int
    external fun openFile(path: String, handle: JNIHandleL): Int
    external fun readFile(handle: Long, size: Long, buffer: ByteArray, actualSize: JNIHandleL): Int
    external fun seekFile(handle: Long, offset: Long, seek: Byte, newPosition: JNIHandleL): Int
    external fun getFileSize(handle: Long, size: JNIHandleL): Int
    external fun closeFile(handle: Long): Int
    external fun openSerializedFile(path: String, handle: JNIHandleL): Int
    external fun closeSerializedFile(handle: Long): Int

    external fun getExternalReferenceCount(handle: Long, count: JNIHandleI): Int
    external fun getExternalReference(handle: Long, nodeIndex: Int, data: ExternalReferenceData): Int
    external fun getObjectCount(handle: Long, count: JNIHandleI): Int
    external fun getObjectInfo(handle: Long, len: Int, data: Array<IObject>): Int
    external fun getTypeTree(handle: Long, objectId: Long, typeTree: JNIHandleL): Int
    external fun getTypeTreeNodeInfo(handle: Long, node: Int, typeTree: TypeTreeNodeInfoData): Int
    external fun getRefTypeTypeTree(handle: Long, className: String, namespaceName: String, assemblyName: String, typeTree: JNIHandleL): Int

    companion object {
        init {
            System.loadLibrary("UnityFileSystemApi")
            System.loadLibrary("kabt-wrapper")
        }
    }
}

data class JNIHandleL(val value: Long) {
    constructor() : this(0)
}
data class JNIHandleI(val value: Int) {
    constructor() : this(0)
}
