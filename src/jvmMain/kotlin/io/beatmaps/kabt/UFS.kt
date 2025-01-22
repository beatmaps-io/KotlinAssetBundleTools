package io.beatmaps.kabt

import io.beatmaps.kabt.external.ArchiveNodeData
import io.beatmaps.kabt.file.UnityFileSystem
import io.beatmaps.kabt.flags.ArchiveNodeFlags

class UFSJNI {
    external fun init(): Int
    external fun cleanup(): Int
    external fun mountArchive(path: String, mountPoint: String, handle: JNIHandle): Int
    external fun getArchiveNodeCount(handle: Long, count: JNIHandle): Int
    external fun getArchiveNode(handle: Long, nodeIndex: Int, data: ArchiveNodeData): Int
    external fun openFile(path: String, handler: JNIHandle): Int

    companion object {
        init {
            System.loadLibrary("UnityFileSystemApi")
            System.loadLibrary("kabt-wrapper")
        }
    }
}

data class JNIHandle(val value: Long) {
    constructor() : this(0L)
}

fun main() {
    try {
        UnityFileSystem().use { ufs ->
            val archive = ufs.mount("C:\\Users\\micro\\Downloads\\VivifyExample - you\\bundleWindows2019.vivify", "/")

            archive.nodes.forEach { node ->
                println("Processing ${node.path} ${node.size} ${node.flags}")

                if (ArchiveNodeFlags.SerializedFile.check(node.flags)) {
                    println("Found ${node.path}")
                    node.getReader().use { reader ->
                        val file = reader.serializedFile

                        file.externalReferences.forEachIndexed { idx, ref ->
                            println("path($idx): \"${ref.path}\" GUID: ${ref.guid} Type: ${ref.externalReferenceType}")
                        }

                        file.objects.filter { obj -> obj.typeId == 142 }.forEach { obj ->
                            val root = file.getTypeTreeRoot(obj.id)
                            // var offset = obj.Offset; // Pass by ref?
                            println("ID: ${obj.id} (Class ID ${obj.typeId}) $root")
                            val sb = StringBuilder()
                            obj.dump(sb, root, Offset(obj.offset), 0)
                        }
                    }
                }
            }

            println("Complete!")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
