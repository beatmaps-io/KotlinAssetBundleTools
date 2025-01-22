package io.beatmaps.kabt.base

interface IUnityArchive : AutoCloseable {
    val nodes: List<IArchiveNode>
}
