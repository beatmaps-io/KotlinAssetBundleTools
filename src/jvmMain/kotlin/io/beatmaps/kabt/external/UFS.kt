package io.beatmaps.kabt.external

import io.beatmaps.kabt.JNIHandle
import io.beatmaps.kabt.SeekOrigin
import io.beatmaps.kabt.UFSJNI
import io.beatmaps.kabt.base.IObject

actual object UFS {
    private val jni = UFSJNI()
    private val long1 = JNIHandle()

    actual fun init() = UFSError.handle(jni.init())
    actual fun cleanup() = UFSError.handle(jni.cleanup())

    actual fun mountArchive(path: String, mountPoint: String): Long {
        UFSError.handle(jni.mountArchive(path, mountPoint, long1))

        return long1.value
    }

    actual fun unmountArchive(handle: Long) {
        TODO("Not yet implemented")
    }

    actual fun getArchiveNodeCount(handle: Long): Long {
        UFSError.handle(jni.getArchiveNodeCount(handle, long1))

        return long1.value
    }

    actual fun getArchiveNode(handle: Long, nodeIndex: Int) =
        ArchiveNodeData().also { data ->
            UFSError.handle(jni.getArchiveNode(handle, nodeIndex, data))
        }

    actual fun openFile(path: String): Long {
        UFSError.handle(
            jni.openFile(path, long1)
        )

        return long1.value
    }

    actual fun readFile(handle: Long, size: Long): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun seekFile(handle: Long, offset: Long, origin: SeekOrigin): Long {
        TODO("Not yet implemented")
    }

    actual fun getFileSize(handle: Long): Long {
        TODO("Not yet implemented")
    }

    actual fun closeFile(handle: Long) {
    }

    actual fun openSerializedFile(path: String): Long {
        TODO("Not yet implemented")
    }

    actual fun closeSerializedFile(handle: Long) {
    }

    actual fun getExternalReferenceCount(handle: Long): Int {
        TODO("Not yet implemented")
    }

    actual fun getExternalReference(handle: Long, index: Int): ExternalReferenceData {
        TODO("Not yet implemented")
    }

    actual fun getObjectCount(handle: Long): Int {
        TODO("Not yet implemented")
    }

    actual fun getObjectInfo(handle: Long, len: Int): List<IObject> {
        TODO("Not yet implemented")
    }

    actual fun getTypeTree(handle: Long, objectId: Long): Long {
        TODO("Not yet implemented")
    }

    actual fun getRefTypeTypeTree(handle: Long, className: String, namespaceName: String, assemblyName: String): Long {
        TODO("Not yet implemented")
    }

    actual fun getTypeTreeNodeInfo(handle: Long, node: Int): TypeTreeNodeInfoData {
        TODO("Not yet implemented")
    }
}