package io.beatmaps.kabt.external

import io.beatmaps.kabt.base.IObject

data class ObjectInfoData(override val id: Long, override val offset: Long, override val size: Long, override val typeId: Int) : IObject
