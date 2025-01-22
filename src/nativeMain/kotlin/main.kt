import io.beatmaps.kabt.flags.ArchiveNodeFlags
import io.beatmaps.kabt.Offset
import io.beatmaps.kabt.file.UnityFileSystem

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
