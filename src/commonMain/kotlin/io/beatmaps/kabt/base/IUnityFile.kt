package io.beatmaps.kabt.base

import io.beatmaps.kabt.SeekOrigin

interface IUnityFile : AutoCloseable {
    val size: Long
    fun seek(offset: Long, origin: SeekOrigin = SeekOrigin.Begin): Long
    fun read(size: Int, buffer: ByteArray): Long
}
