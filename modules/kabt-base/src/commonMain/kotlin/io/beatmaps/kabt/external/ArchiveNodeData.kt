package io.beatmaps.kabt.external

import io.beatmaps.kabt.base.IArchiveNodeData

data class ArchiveNodeData(override val path: String, override val size: Int, override val flags: Int) : IArchiveNodeData {
    constructor() : this("", 0, 0)
}
