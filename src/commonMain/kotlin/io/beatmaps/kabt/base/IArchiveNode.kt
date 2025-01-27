package io.beatmaps.kabt.base

interface IArchiveNode : AutoCloseable {
    val path: String
    val size: Int
    val flags: Int

    val reader: IUnityFileReader
}
