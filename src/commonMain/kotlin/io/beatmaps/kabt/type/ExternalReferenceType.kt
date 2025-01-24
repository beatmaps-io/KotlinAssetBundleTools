package io.beatmaps.kabt.type

enum class ExternalReferenceType(val code: Int) {
    NonAssetType(0),
    DeprecatedCachedAssetType(1),
    SerializedAssetType(2),
    MetaAssetType(3);

    companion object {
        private val map = entries.associateBy { it.code }
        fun fromInt(code: Int) = map.getValue(code)
    }
}
