package io.beatmaps.kabt.external

import io.beatmaps.kabt.JNIHandleI
import io.beatmaps.kabt.JNIHandleL
import io.beatmaps.kabt.SeekOrigin
import io.beatmaps.kabt.UFSJNI
import io.beatmaps.kabt.base.IObject

actual object UFS {
    private val jni = UFSJNI()
    private val long1 = JNIHandleL()
    private val int1 = JNIHandleI()

    actual fun init() = UFSError.handle(jni.init())
    actual fun cleanup() = UFSError.handle(jni.cleanup())

    actual fun mountArchive(path: String, mountPoint: String): Long {
        UFSError.handle(jni.mountArchive(path, mountPoint, long1))

        return long1.value
    }

    actual fun unmountArchive(handle: Long) =
        UFSError.handle(
            jni.unmountArchive(handle)
        )

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

    actual fun readFile(handle: Long, size: Long) =
        ByteArray(size.toInt()).also {
            UFSError.handle(
                jni.readFile(handle, size, it, long1)
            )
        }.sliceArray(0..<long1.value.toInt()).toUByteArray()

    actual fun seekFile(handle: Long, offset: Long, origin: SeekOrigin): Long {
        UFSError.handle(
            jni.seekFile(handle, offset, origin.code, long1)
        )

        return long1.value
    }

    actual fun getFileSize(handle: Long): Long {
        UFSError.handle(
            jni.getFileSize(handle, long1)
        )

        return long1.value
    }

    actual fun closeFile(handle: Long) =
        UFSError.handle(
            jni.closeFile(handle)
        )

    actual fun openSerializedFile(path: String): Long {
        UFSError.handle(
            jni.openSerializedFile(path, long1)
        )

        return long1.value
    }

    actual fun closeSerializedFile(handle: Long) =
        UFSError.handle(
            jni.closeSerializedFile(handle)
        )

    actual fun getExternalReferenceCount(handle: Long): Int {
        UFSError.handle(
            jni.getExternalReferenceCount(handle, int1)
        )

        return int1.value
    }

    actual fun getExternalReference(handle: Long, index: Int) =
        ExternalReferenceData().also { data ->
            UFSError.handle(jni.getExternalReference(handle, index, data))
        }

    actual fun getObjectCount(handle: Long): Int {
        UFSError.handle(
            jni.getObjectCount(handle, int1)
        )

        return int1.value
    }

    actual fun getObjectInfo(handle: Long, len: Int): List<IObject> =
        Array<IObject>(len) { ObjectInfoData() }.also { data ->
            UFSError.handle(
                jni.getObjectInfo(handle, len, data)
            )
        }.toList()

    actual fun getTypeTree(handle: Long, objectId: Long): Long {
        UFSError.handle(
            jni.getTypeTree(handle, objectId, long1)
        )

        return long1.value
    }

    actual fun getRefTypeTypeTree(handle: Long, className: String, namespaceName: String, assemblyName: String): Long {
        UFSError.handle(
            jni.getRefTypeTypeTree(handle, className, namespaceName, assemblyName, long1)
        )

        return long1.value
    }

    actual fun getTypeTreeNodeInfo(handle: Long, node: Int) =
        TypeTreeNodeInfoData().also {
            UFSError.handle(
                jni.getTypeTreeNodeInfo(handle, node, it)
            )
        }
}
