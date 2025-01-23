package io.beatmaps.kabt.base

interface IArchiveNode {
    val path: String
    val size: Int
    val flags: Int

    fun getReader(bufferSize: Int = 64 * 1024 * 1024): IUnityFileReader
}
