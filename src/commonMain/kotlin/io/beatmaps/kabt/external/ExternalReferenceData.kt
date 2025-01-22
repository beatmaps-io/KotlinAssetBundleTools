package io.beatmaps.kabt.external

data class ExternalReferenceData(val path: String, val guid: String, val type: Int) {
    constructor() : this("", "", 0)
}
