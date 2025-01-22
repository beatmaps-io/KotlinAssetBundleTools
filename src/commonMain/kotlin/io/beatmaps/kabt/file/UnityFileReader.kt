package io.beatmaps.kabt.file

import io.beatmaps.kabt.ExternalType
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.exception.IOException
import io.beatmaps.kabt.external.UFS

class UnityFileReader(private val ufs: UnityFileSystem, private val path: String, private val bufferSize: Int) : IUnityFileReader {
    private val file = UnityFile(UFS.openFile(path))
    override val length = file.size

    private val buffer = ByteArray(bufferSize)
    private var bufferStartInFile = 0L
    private var bufferEndInFile = 0L

    override val serializedFile by lazy {
        SerializedFile(ufs, this, UFS.openSerializedFile(path))
    }

    override fun close() {
        file.close()
        serializedFile.close()
    }

    override fun readString(fileOffset: Long, size: Int) =
        readBytes(fileOffset, size).let { offset ->
            buffer.decodeToString(offset, offset + size)
        }

    override fun readInt8(fileOffset: Long) =
        readBytes(fileOffset, ExternalType.Int8.size).let { offset ->
            buffer[offset]
        }

    override fun readInt16(fileOffset: Long) =
        readBytes(fileOffset, ExternalType.Int16.size).let { offset ->
            buffer[offset] + (buffer[offset + 1].toInt() shl 8)
        }.toShort()

    override fun readInt32(fileOffset: Long) =
        readBytes(fileOffset, ExternalType.Int32.size).let { offset ->
            buffer[offset] +
                (buffer[offset + 1].toInt() shl 8) +
                (buffer[offset + 2].toInt() shl 16) +
                (buffer[offset + 3].toInt() shl 24)
        }

    override fun readInt64(fileOffset: Long) =
        readBytes(fileOffset, ExternalType.Int64.size).let { offset ->
            buffer[offset] +
                    (buffer[offset + 1].toLong() shl 8) +
                    (buffer[offset + 2].toLong() shl 16) +
                    (buffer[offset + 3].toLong() shl 24) +
                    (buffer[offset + 4].toLong() shl 32) +
                    (buffer[offset + 5].toLong() shl 40) +
                    (buffer[offset + 6].toLong() shl 48) +
                    (buffer[offset + 7].toLong() shl 56)
        }

    override fun readArray(fileOffset: Long, size: Int, out: ByteArray) {
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
}
