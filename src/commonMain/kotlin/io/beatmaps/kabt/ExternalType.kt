package io.beatmaps.kabt

enum class ExternalType(val size: Int) {
    Int32(4), UInt32(4),
    Float(4), Double(8),
    Int16(2), UInt16(2),
    Int64(8), UInt64(8),
    Int8(1), UInt8(1),
    Bool(1);
}
