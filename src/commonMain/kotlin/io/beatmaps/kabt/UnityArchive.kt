package io.beatmaps.kabt

import io.beatmaps.kabt.base.IUnityArchive
import io.beatmaps.kabt.external.UFS
import io.beatmaps.kabt.file.UnityFileSystem

class UnityArchive(private val ufs: UnityFileSystem, private val mountPoint: String, private val handle: Long) : IUnityArchive {
    override val nodes by lazy {
        val count = UFS.getArchiveNodeCount(handle)

        if (count == 0L) emptyList<ArchiveNode>()

        List(count.toInt()) { idx ->
            UFS.getArchiveNode(handle, idx).let { data ->
                ArchiveNode(ufs, "$mountPoint${data.path}", data.size, data.flags)
            }
        }
    }

    override fun close() {
        UFS.unmountArchive(handle)
    }
}
