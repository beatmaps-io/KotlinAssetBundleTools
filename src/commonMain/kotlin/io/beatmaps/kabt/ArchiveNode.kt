package io.beatmaps.kabt

import io.beatmaps.kabt.base.IArchiveNode
import io.beatmaps.kabt.file.UnityFileReader
import io.beatmaps.kabt.file.UnityFileSystem
import io.beatmaps.kabt.flags.ArchiveNodeFlags

data class ArchiveNode(private val ufs: UnityFileSystem, override val path: String, override val size: Int, override val flags: Int) : IArchiveNode {
    val isSerializedFile = ArchiveNodeFlags.SerializedFile.check(flags)

    private val readerLazy = lazy { UnityFileReader(ufs, path, 64 * 1024 * 1024) }
    override val reader by readerLazy

    override fun close() {
        if (readerLazy.isInitialized()) reader.close()
    }
}
