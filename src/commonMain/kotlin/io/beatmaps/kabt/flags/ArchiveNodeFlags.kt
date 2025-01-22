package io.beatmaps.kabt.flags

enum class ArchiveNodeFlags(private val flag: Int) {
    None(0),
    Directory(1),
    Deleted(2),
    SerializedFile(4);

    fun check(flags: Int) = flags and flag != 0
}
