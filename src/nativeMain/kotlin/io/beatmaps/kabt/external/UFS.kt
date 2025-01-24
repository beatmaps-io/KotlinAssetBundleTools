package io.beatmaps.kabt.external

import io.beatmaps.kabt.SeekOrigin
import io.beatmaps.kabt.base.IObject
import io.beatmaps.kabt.exception.InvalidOperationException
import io.beatmaps.kabt.unity.ObjectInfo
import io.beatmaps.kabt.unity.UFS_Cleanup
import io.beatmaps.kabt.unity.UFS_CloseFile
import io.beatmaps.kabt.unity.UFS_CloseSerializedFile
import io.beatmaps.kabt.unity.UFS_GetArchiveNode
import io.beatmaps.kabt.unity.UFS_GetArchiveNodeCount
import io.beatmaps.kabt.unity.UFS_GetExternalReference
import io.beatmaps.kabt.unity.UFS_GetExternalReferenceCount
import io.beatmaps.kabt.unity.UFS_GetFileSize
import io.beatmaps.kabt.unity.UFS_GetObjectCount
import io.beatmaps.kabt.unity.UFS_GetObjectInfo
import io.beatmaps.kabt.unity.UFS_GetRefTypeTypeTree
import io.beatmaps.kabt.unity.UFS_GetTypeTree
import io.beatmaps.kabt.unity.UFS_GetTypeTreeNodeInfo
import io.beatmaps.kabt.unity.UFS_Init
import io.beatmaps.kabt.unity.UFS_MountArchive
import io.beatmaps.kabt.unity.UFS_OpenFile
import io.beatmaps.kabt.unity.UFS_OpenSerializedFile
import io.beatmaps.kabt.unity.UFS_ReadFile
import io.beatmaps.kabt.unity.UFS_SeekFile
import io.beatmaps.kabt.unity.UFS_UnmountArchive
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value

actual object UFS {
    private const val BUFFER_1_SIZE = 512u
    private val buffer1 = nativeHeap.allocArray<ByteVar>(BUFFER_1_SIZE.toInt())

    private const val BUFFER_2_SIZE = 512u
    private val buffer2 = nativeHeap.allocArray<ByteVar>(BUFFER_2_SIZE.toInt())

    private val long1: ULongVar = nativeHeap.alloc()

    private val int1: UIntVar = nativeHeap.alloc()
    private val int2: UIntVar = nativeHeap.alloc()
    private val int3: UIntVar = nativeHeap.alloc()
    private val int4: UIntVar = nativeHeap.alloc()
    private val int5: UIntVar = nativeHeap.alloc()

    private val byte1: ByteVar = nativeHeap.alloc()

    actual fun init() = UFSError.handle(UFS_Init())
    actual fun cleanup() = UFSError.handle(UFS_Cleanup())

    actual fun mountArchive(path: String, mountPoint: String): Long {
        UFSError.handle(
            UFS_MountArchive(path, mountPoint, long1.ptr)
        )

        return long1.value.toLong()
    }

    actual fun unmountArchive(handle: Long) = UFSError.handle(UFS_UnmountArchive(handle.toULong()))

    actual fun getArchiveNodeCount(handle: Long): Long {
        UFSError.handle(
            UFS_GetArchiveNodeCount(handle.toULong(), long1.ptr)
        )

        return long1.value.toLong()
    }

    actual fun getArchiveNode(handle: Long, nodeIndex: Int): ArchiveNodeData {
        UFSError.handle(
            UFS_GetArchiveNode(handle.toULong(), nodeIndex.toUInt(), buffer1, BUFFER_1_SIZE, int1.ptr, int2.ptr)
        )

        return ArchiveNodeData(buffer1.toKString(), int1.value.toInt(), int2.value.toInt())
    }

    actual fun openFile(path: String): Long {
        UFSError.handle(
            UFS_OpenFile(path, long1.ptr)
        )

        return long1.value.toLong()
    }

    actual fun readFile(handle: Long, size: Long) = memScoped {
        val cBuffer = allocArray<ByteVar>(size)

        UFSError.handle(
            UFS_ReadFile(handle.toULong(), size.toULong(), cBuffer, long1.ptr)
        )

        if (long1.value > Int.MAX_VALUE.toUInt())
            throw InvalidOperationException("Can't read files longer than max int bytes")

        cBuffer.readBytes(long1.value.toInt()).toUByteArray()
    }

    actual fun seekFile(handle: Long, offset: Long, origin: SeekOrigin): Long {
        UFSError.handle(
            UFS_SeekFile(handle.toULong(), offset.toULong(), origin.code, long1.ptr)
        )

        return long1.value.toLong()
    }

    actual fun getFileSize(handle: Long): Long {
        UFSError.handle(
            UFS_GetFileSize(handle.toULong(), long1.ptr)
        )

        return long1.value.toLong()
    }

    actual fun closeFile(handle: Long) = UFSError.handle(UFS_CloseFile(handle.toULong()))

    actual fun openSerializedFile(path: String): Long {
        UFSError.handle(
            UFS_OpenSerializedFile(path, long1.ptr)
        )

        return long1.value.toLong()
    }

    actual fun closeSerializedFile(handle: Long) = UFSError.handle(UFS_CloseSerializedFile(handle.toULong()))

    actual fun getExternalReferenceCount(handle: Long): Int {
        UFSError.handle(
            UFS_GetExternalReferenceCount(handle.toULong(), int1.ptr)
        )

        return int1.value.toInt()
    }

    actual fun getExternalReference(handle: Long, index: Int): ExternalReferenceData {
        UFSError.handle(
            UFS_GetExternalReference(handle.toULong(), index.toUInt(), buffer1, BUFFER_1_SIZE, buffer2, int1.ptr)
        )

        return ExternalReferenceData(buffer1.toKString(), buffer2.toKString(), int1.value.toInt())
    }

    actual fun getObjectCount(handle: Long): Int {
        UFSError.handle(
            UFS_GetObjectCount(handle.toULong(), int1.ptr)
        )

        return int1.value.toInt()
    }

    actual fun getObjectInfo(handle: Long, len: Int): List<IObject> = memScoped {
        val arr = allocArray<ObjectInfo>(len)
        UFSError.handle(
            UFS_GetObjectInfo(handle.toULong(), arr, len.toUInt())
        )

        return List(len) { idx ->
            val obj = arr[idx]
            ObjectInfoData(obj.id.toLong(), obj.offset.toLong(), obj.size.toLong(), obj.typeId.toInt())
        }
    }

    actual fun getTypeTree(handle: Long, objectId: Long): Long {
        UFSError.handle(
            UFS_GetTypeTree(handle.toULong(), objectId.toULong(), long1.ptr)
        )

        return long1.value.toLong()
    }

    actual fun getRefTypeTypeTree(handle: Long, className: String, namespaceName: String, assemblyName: String): Long {
        UFSError.handle(
            UFS_GetRefTypeTypeTree(handle.toULong(), className, namespaceName, assemblyName, long1.ptr)
        )

        return long1.value.toLong()
    }

    actual fun getTypeTreeNodeInfo(handle: Long, node: Int): TypeTreeNodeInfoData {
        UFSError.handle(
            UFS_GetTypeTreeNodeInfo(handle.toULong(), node.toUInt(), buffer1, BUFFER_1_SIZE, buffer2, BUFFER_2_SIZE, int1.ptr, int2.ptr, byte1.ptr, int3.ptr, int4.ptr, int5.ptr)
        )

        return TypeTreeNodeInfoData(buffer1.toKString(), buffer2.toKString(), int1.value.toInt(), int2.value.toInt(), byte1.value, int3.value.toInt(), int4.value.toInt(), int5.value.toInt())
    }
}
