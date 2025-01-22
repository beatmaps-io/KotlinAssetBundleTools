package io.beatmaps.kabt.base

import io.beatmaps.kabt.ExternalType

interface IUnityFileReader : AutoCloseable {
    val length: Long
    val serializedFile: ISerializedFile

    fun readValue(node: TypeTreeNodeBase, offset: Long) = readValue(node.kotlinType, offset)
    fun readValue(type: ExternalType?, offset: Long) =
        when (type) {
            ExternalType.Int32 -> readInt32(offset)
            ExternalType.UInt32 -> readUInt32(offset)
            ExternalType.Float -> readFloat(offset)
            ExternalType.Double -> readDouble(offset)
            ExternalType.Int16 -> readInt16(offset)
            ExternalType.UInt16 -> readUInt16(offset)
            ExternalType.Int64 -> readInt64(offset)
            ExternalType.UInt64 -> readUInt64(offset)
            ExternalType.Int8 -> readInt8(offset)
            ExternalType.UInt8 -> readUInt8(offset)
            ExternalType.Bool -> (readUInt8(offset) != 0.toUByte())
            else -> throw Exception("Can't get value of type $type")
        }

    fun readString(fileOffset: Long, size: Int): String

    fun readFloat(fileOffset: Long) = Float.fromBits(readInt32(fileOffset))
    fun readDouble(fileOffset: Long) = Double.fromBits(readInt64(fileOffset))

    fun readUInt8(fileOffset: Long) = readInt8(fileOffset).toUByte()
    fun readInt8(fileOffset: Long): Byte

    fun readUInt16(fileOffset: Long) = readInt16(fileOffset).toUShort()
    fun readInt16(fileOffset: Long): Short

    fun readUInt32(fileOffset: Long) = readInt32(fileOffset).toUInt()
    fun readInt32(fileOffset: Long): Int

    fun readUInt64(fileOffset: Long) = readInt64(fileOffset).toULong()
    fun readInt64(fileOffset: Long): Long

    fun readArray(fileOffset: Long, size: Int, out: ByteArray)
    fun readArray(type: ExternalType, fileOffset: Long, size: Int): List<Any>
}
