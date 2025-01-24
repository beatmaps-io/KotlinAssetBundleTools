package io.beatmaps.kabt

import io.beatmaps.kabt.type.ExternalReferenceType

data class ExternalReference(val path: String, val guid: String, val externalReferenceType: ExternalReferenceType)
