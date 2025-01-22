package io.beatmaps.kabt.flags

enum class TreeTypeFlags(private val flag: Int) {
    None(-1),
    IsArray(0),
    IsManagedReference(1),
    IsManagedReferenceRegistry(2),
    IsArrayOfRefs(3);

    fun check(flags: Byte) = flags.toInt() and (1 shl flag) != 0
}
