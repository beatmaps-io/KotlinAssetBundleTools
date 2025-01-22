package io.beatmaps.kabt.external

data class ArchiveNodeData(val path: String, val size: Int, val flags: Int) {
    constructor() : this("", 0, 0)
}
