package io.beatmaps.kabt

data class Offset(var offset: Long) {
    fun plus(other: Int) = also {
        offset += other
    }

    fun align() {
        offset = (offset + 3) and 3.inv()
    }
}
