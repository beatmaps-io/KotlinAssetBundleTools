package io.beatmaps.kabt

class Handle private constructor(val long: Long, val uLong: ULong) {
    constructor(handle: Long, ignored: Boolean = false) : this(handle, handle.toULong())
    constructor(handle: ULong) : this(handle.toLong(), handle)
}
