package io.beatmaps.kabt.file

import io.beatmaps.kabt.type.ExternalType
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.exception.IOException
import io.beatmaps.kabt.external.UFS

class UnityFileReader(private val ufs: UnityFileSystem, private val path: String, private val bufferSize: Int) : IUnityFileReader {
    private val file = UnityFile(UFS.openFile(path))
    override val length = file.size

    private val buffer = UByteArray(bufferSize)
    private var bufferStartInFile = 0L
    private var bufferEndInFile = 0L

    private val serializedFileLazy = lazy {
        SerializedFile(ufs, this, UFS.openSerializedFile(path))
    }
    override val serializedFile by serializedFileLazy

    override fun close() {
        file.close()

        if (serializedFileLazy.isInitialized()) {
            serializedFile.close()
        }
    }

    override fun readString(fileOffset: Long, size: Int) =
        readBytes(fileOffset, size).let { offset ->
            buffer.copyOfRange(offset, offset + size).toByteArray().decodeToString()
        }

    override fun readUInt8(fileOffset: Long) =
        readBytes(fileOffset, ExternalType.Int8.size).let { offset ->
            buffer[offset]
        }

    override fun readUInt16(fileOffset: Long) =
        readBytes(fileOffset, ExternalType.Int16.size).let { offset ->
            buffer[offset] + (buffer[offset + 1].toUInt() shl 8)
        }.toUShort()

    override fun readUInt32(fileOffset: Long) =
        readBytes(fileOffset, ExternalType.Int32.size).let { offset ->
            buffer[offset] +
                (buffer[offset + 1].toUInt() shl 8) +
                (buffer[offset + 2].toUInt() shl 16) +
                (buffer[offset + 3].toUInt() shl 24)
        }

    override fun readUInt64(fileOffset: Long) =
        readBytes(fileOffset, ExternalType.Int64.size).let { offset ->
            buffer[offset] +
                    (buffer[offset + 1].toULong() shl 8) +
                    (buffer[offset + 2].toULong() shl 16) +
                    (buffer[offset + 3].toULong() shl 24) +
                    (buffer[offset + 4].toULong() shl 32) +
                    (buffer[offset + 5].toULong() shl 40) +
                    (buffer[offset + 6].toULong() shl 48) +
                    (buffer[offset + 7].toULong() shl 56)
        }

    override fun readArray(fileOffset: Long, size: Int, out: UByteArray) {
        val offset = readBytes(fileOffset, size)
        buffer.copyInto(out, 0, offset, size)
    }

    override fun readArray(type: ExternalType, fileOffset: Long, size: Int): List<Any> {
        // Load bytes into buffer
        readBytes(fileOffset, size * type.size)

        return List(size) { idx -> readValue(type, fileOffset + (idx * type.size)) }
    }

    private fun readBytes(fileOffset: Long, count: Int): Int {
        if (fileOffset < bufferStartInFile || fileOffset + count > bufferEndInFile) {
            if (count > bufferSize) throw IOException("Requested size is larger than cache size")

            bufferStartInFile = file.seek(fileOffset)

            if (bufferStartInFile != fileOffset) throw IOException("Invalid file offset")

            val actualSize = file.read(bufferSize, buffer)
            bufferEndInFile = bufferStartInFile + actualSize
        }

        return (fileOffset - bufferStartInFile).toInt()
    }

    fun crc32(previous: UInt): UInt
    {
        val fileOffset = 0L
        val readSize = if (length > bufferSize) bufferSize else length.toInt()
        var readBytes = 0
        var crc32 = previous

        while (readBytes < length)
        {
            val offset2 = readBytes(fileOffset, readSize)
            val end = offset2 + readSize
            crc32 = CRC32.calculateCRC32(buffer.sliceArray(offset2..<end), crc32)
            readBytes += readSize
        }

        return crc32
    }
}
