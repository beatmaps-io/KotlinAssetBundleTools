package io.beatmaps.kabt.file

import io.beatmaps.kabt.UnityArchive
import io.beatmaps.kabt.external.UFS

class UnityFileSystem : AutoCloseable {
    init {
        UFS.init()
    }

    fun mount(path: String, mountPoint: String) =
        UnityArchive(this, mountPoint, UFS.mountArchive(path, mountPoint))

    override fun close() {
        UFS.cleanup()
    }
}
