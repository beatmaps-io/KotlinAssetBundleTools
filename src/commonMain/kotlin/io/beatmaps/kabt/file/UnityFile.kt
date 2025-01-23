package io.beatmaps.kabt.file

import io.beatmaps.kabt.SeekOrigin
import io.beatmaps.kabt.base.IUnityFile
import io.beatmaps.kabt.external.UFS

class UnityFile(private val handle: Long) : IUnityFile {
    override val size by lazy {
        UFS.getFileSize(handle)
    }

    override fun seek(offset: Long, origin: SeekOrigin) =
        UFS.seekFile(handle, offset, origin)

    override fun read(size: Int, buffer: UByteArray): Long =
        UFS.readFile(handle, size.toLong()).also {
            it.copyInto(buffer)
        }.size.toLong()

    override fun close() {
        UFS.closeFile(handle)
    }
}
