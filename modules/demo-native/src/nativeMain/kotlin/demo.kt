import io.beatmaps.kabt.UnityArchive
import io.beatmaps.kabt.file.UnityFileSystem
import io.beatmaps.kabt.flags.ArchiveNodeFlags
import io.beatmaps.kabt.tree.ComplexAsset
import io.beatmaps.kabt.tree.MapAsset
import io.beatmaps.kabt.tree.StringAsset
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.getcwd

fun getCwd(): String? {
    return ByteArray(1024).usePinned { getcwd(it.addressOf(0), 1024) }?.toKString()
}

fun main() {
    try {
        UnityFileSystem().use { ufs ->
            ufs.mount("${getCwd()}/bundle/example.bundle", "/").use { archive ->
                println("Result: ${archive.crc32} == 391718962")

                val assets = archive.nodes.filter { it.isSerializedFile }.mapNotNull { node ->
                    (node.reader.serializedFile.objects.firstOrNull { obj -> obj.typeId == 142 }?.asset as? ComplexAsset)?.let { rootAsset ->
                        val container = rootAsset.children.filterIsInstance<MapAsset>().firstOrNull { it.name == "m_Container" }
                        container?.map?.map { it.key }?.filterIsInstance<StringAsset>()?.map { it.string }
                    }
                }.flatten()

                println(assets)
            }
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
            val file = node.reader.serializedFile

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
