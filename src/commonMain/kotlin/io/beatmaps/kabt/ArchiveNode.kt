package io.beatmaps.kabt

import io.beatmaps.kabt.base.IArchiveNode
import io.beatmaps.kabt.file.UnityFileReader
import io.beatmaps.kabt.file.UnityFileSystem

data class ArchiveNode(private val ufs: UnityFileSystem, override val path: String, override val size: Int, override val flags: Int) : IArchiveNode {
    override fun getReader() = UnityFileReader(ufs, path, 64 * 1024 * 1024)
}
