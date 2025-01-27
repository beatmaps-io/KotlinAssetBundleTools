package io.beatmaps.kabt

import io.beatmaps.kabt.base.IUnityArchive
import io.beatmaps.kabt.external.UFS
import io.beatmaps.kabt.file.UnityFileSystem

class UnityArchive(private val ufs: UnityFileSystem, private val mountPoint: String, private val handle: Handle) : IUnityArchive {
    private val nodeLazy = lazy {
        val count = UFS.getArchiveNodeCount(handle)

        if (count == 0L) emptyList<ArchiveNode>()

        List(count.toInt()) { idx ->
            UFS.getArchiveNode(handle, idx).let { data ->
                ArchiveNode(ufs, "$mountPoint${data.path}", data.size, data.flags)
            }
        }
    }

    override val nodes by nodeLazy

    val crc32 by lazy {
        nodes.fold(0u) { crc32, node ->
            node.reader.crc32(crc32)
        }
    }

    override fun close() {
        if (nodeLazy.isInitialized()) {
            nodes.forEach { it.close() }
        }

        UFS.unmountArchive(handle)
    }
}
