package io.beatmaps.kabt.external

import io.beatmaps.kabt.base.IExternalReferenceData

data class ExternalReferenceData(override val path: String, override val guid: String, override val type: Int) : IExternalReferenceData {
    constructor() : this("", "", 0)
}
