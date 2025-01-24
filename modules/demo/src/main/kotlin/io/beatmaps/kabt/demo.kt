package io.beatmaps.kabt

import io.beatmaps.kabt.file.UnityFileSystem
import io.beatmaps.kabt.flags.ArchiveNodeFlags
import io.beatmaps.kabt.tree.ComplexAsset
import io.beatmaps.kabt.tree.MapAsset
import io.beatmaps.kabt.tree.StringAsset

fun main() {
    try {
        UnityFileSystem().use { ufs ->
            val archive = ufs.mount("C:\\Users\\micro\\Downloads\\VivifyExample - you\\bundleWindows2019.vivify", "/")

            val assets = archive.nodes.filter { it.isSerializedFile }.mapNotNull { node ->
                node.getReader().use { reader ->
                    (reader.serializedFile.objects.firstOrNull { obj -> obj.typeId == 142 }?.asset as? ComplexAsset)?.let { rootAsset ->
                        val container = rootAsset.children.filterIsInstance<MapAsset>().firstOrNull { it.name == "m_Container" }
                        container?.map?.map { it.key }?.filterIsInstance<StringAsset>()?.map { it.string }
                    }
                }
            }.flatten()

            println(assets)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun dump(archive: UnityArchive) {
    archive.nodes.forEach { node ->
        println("Processing ${node.path} ${node.size} ${node.flags}")

        if (ArchiveNodeFlags.SerializedFile.check(node.flags)) {
            println("Found ${node.path}")
            node.getReader().use { reader ->
                val file = reader.serializedFile

                file.externalReferences.forEachIndexed { idx, ref ->
                    println("path($idx): \"${ref.path}\" GUID: ${ref.guid} Type: ${ref.externalReferenceType}")
                }

                file.objects.forEach { obj ->
                    println("ID: ${obj.id} (Class ID ${obj.typeId})")
                    val sb = StringBuilder()
                    obj.dump(sb)

                    println(sb)
                }
            }
        }
    }
}
