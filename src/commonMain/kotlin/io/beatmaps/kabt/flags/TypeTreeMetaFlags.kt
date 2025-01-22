package io.beatmaps.kabt.flags

enum class TypeTreeMetaFlags(private val flag: Int) {
    None(-1),
    AlignBytes(14),
    AnyChildUsesAlignBytes(15);

    fun check(flags: Int) = flags and (1 shl flag) != 0
}
