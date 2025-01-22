package io.beatmaps.kabt.base

interface IArchiveNode {
    val path: String
    val size: Int
    val flags: Int

    fun getReader(): IUnityFileReader
}
