package io.beatmaps.kabt.external

import io.beatmaps.kabt.Handle
import io.beatmaps.kabt.JNIHandleI
import io.beatmaps.kabt.JNIHandleL
import io.beatmaps.kabt.SeekOrigin
import io.beatmaps.kabt.UFSJNI
import io.beatmaps.kabt.base.IArchiveNodeData
import io.beatmaps.kabt.base.IExternalReferenceData
import io.beatmaps.kabt.base.IObject
import io.beatmaps.kabt.base.ITypeTreeNodeInfoData

actual object UFS {
    private val jni = UFSJNI()
    private val long1 = JNIHandleL()
    private val int1 = JNIHandleI()

    actual fun init() = UFSError.handle(jni.init())
    actual fun cleanup() = UFSError.handle(jni.cleanup())

    actual fun mountArchive(path: String, mountPoint: String): Handle {
        UFSError.handle(jni.mountArchive(path, mountPoint, long1))

        return Handle(long1.value)
    }

    actual fun unmountArchive(handle: Handle) =
        UFSError.handle(
            jni.unmountArchive(handle.long)
        )

    actual fun getArchiveNodeCount(handle: Handle): Long {
        UFSError.handle(jni.getArchiveNodeCount(handle.long, long1))

        return long1.value
    }

    actual fun getArchiveNode(handle: Handle, nodeIndex: Int): IArchiveNodeData =
        ArchiveNodeData().also { data ->
            UFSError.handle(jni.getArchiveNode(handle.long, nodeIndex, data))
        }

    actual fun openFile(path: String): Handle {
        UFSError.handle(
            jni.openFile(path, long1)
        )

        return Handle(long1.value)
    }

    actual fun readFile(handle: Handle, size: Long) =
        ByteArray(size.toInt()).also {
            UFSError.handle(
                jni.readFile(handle.long, size, it, long1)
            )
        }.sliceArray(0..<long1.value.toInt()).toUByteArray()

    actual fun seekFile(handle: Handle, offset: Long, origin: SeekOrigin): Long {
        UFSError.handle(
            jni.seekFile(handle.long, offset, origin.code, long1)
        )

        return long1.value
    }

    actual fun getFileSize(handle: Handle): Long {
        UFSError.handle(
            jni.getFileSize(handle.long, long1)
        )

        return long1.value
    }

    actual fun closeFile(handle: Handle) =
        UFSError.handle(
            jni.closeFile(handle.long)
        )

    actual fun openSerializedFile(path: String): Handle {
        UFSError.handle(
            jni.openSerializedFile(path, long1)
        )

        return Handle(long1.value)
    }

    actual fun closeSerializedFile(handle: Handle) =
        UFSError.handle(
            jni.closeSerializedFile(handle.long)
        )

    actual fun getExternalReferenceCount(handle: Handle): Int {
        UFSError.handle(
            jni.getExternalReferenceCount(handle.long, int1)
        )

        return int1.value
    }

    actual fun getExternalReference(handle: Handle, index: Int): IExternalReferenceData =
        ExternalReferenceData().also { data ->
            UFSError.handle(jni.getExternalReference(handle.long, index, data))
        }

    actual fun getObjectCount(handle: Handle): Int {
        UFSError.handle(
            jni.getObjectCount(handle.long, int1)
        )

        return int1.value
    }

    actual fun getObjectInfo(handle: Handle, len: Int): List<IObject> =
        Array<IObject>(len) { ObjectInfoData() }.also { data ->
            UFSError.handle(
                jni.getObjectInfo(handle.long, len, data)
            )
        }.toList()

    actual fun getTypeTree(handle: Handle, objectId: Long): Handle {
        UFSError.handle(
            jni.getTypeTree(handle.long, objectId, long1)
        )

        return Handle(long1.value)
    }

    actual fun getRefTypeTypeTree(handle: Handle, className: String, namespaceName: String, assemblyName: String): Handle {
        UFSError.handle(
            jni.getRefTypeTypeTree(handle.long, className, namespaceName, assemblyName, long1)
        )

        return Handle(long1.value)
    }

    actual fun getTypeTreeNodeInfo(handle: Handle, node: Int): ITypeTreeNodeInfoData =
        TypeTreeNodeInfoData().also {
            UFSError.handle(
                jni.getTypeTreeNodeInfo(handle.long, node, it)
            )
        }
}
