package io.beatmaps.kabt

import io.beatmaps.kabt.base.IObject
import io.beatmaps.kabt.external.ArchiveNodeData
import io.beatmaps.kabt.external.ExternalReferenceData
import io.beatmaps.kabt.external.TypeTreeNodeInfoData
import java.io.File
import java.nio.file.Files

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
        private val path = System.getProperty("java.io.tmpdir")

        private val osSlug = System.getProperty("os.name").lowercase().replace(" ", "").let { osName ->
            setOf("winsows", "macos").firstOrNull { osName.contains(it) } ?: "linux"
        }

        private fun extractLibrary(name: String) {
            try {
                System.loadLibrary(name)
            } catch (linkError: UnsatisfiedLinkError) {
                val file = System.mapLibraryName(name).removePrefix("lib")
                val dest = File(path, file)

                UFSJNI::class.java.classLoader.getResource("kabt/$osSlug/$file")?.let { resource ->
                    if (dest.exists()) {
                        val lastModified = File(resource.path.substring(5, resource.path.indexOf('!'))).lastModified()
                        val destModified = dest.lastModified()
                        if (lastModified > destModified) {
                            dest.delete()
                        }
                    }

                    if (!dest.exists()) {
                        resource.openStream().use { source ->
                            Files.copy(source, dest.toPath())
                        }
                    }
                }

                System.load(dest.toString())
            }
        }

        init {
            extractLibrary("UnityFileSystemApi")
            extractLibrary("kabt-jni")
        }
    }
}

data class JNIHandleL(val value: Long) {
    constructor() : this(0)
}
data class JNIHandleI(val value: Int) {
    constructor() : this(0)
}
