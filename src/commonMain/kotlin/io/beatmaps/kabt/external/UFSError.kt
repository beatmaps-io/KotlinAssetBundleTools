package io.beatmaps.kabt.external

import io.beatmaps.kabt.ReturnCode
import io.beatmaps.kabt.exception.FileNotFoundException
import io.beatmaps.kabt.exception.IOException
import io.beatmaps.kabt.exception.InvalidOperationException

object UFSError {
    fun handle(ret: Int, filename: String = "") = handle(ReturnCode.fromInt(ret), filename)

    private fun handle(ret: ReturnCode, filename: String = "") {
        when (ret) {
            ReturnCode.AlreadyInitialized -> throw InvalidOperationException("UnityFileSystem is already initialized.")
            ReturnCode.NotInitialized -> throw InvalidOperationException("UnityFileSystem is not initialized.")
            ReturnCode.FileNotFound -> throw FileNotFoundException("File not found.", filename)
            ReturnCode.FileFormatError -> throw UnsupportedOperationException("Invalid file format reading $filename.")
            ReturnCode.InvalidArgument -> throw IllegalArgumentException()
            ReturnCode.HigherSerializedFileVersion -> throw UnsupportedOperationException("SerializedFile version not supported.")
            ReturnCode.DestinationBufferTooSmall -> throw IllegalArgumentException("Destination buffer too small.")
            ReturnCode.InvalidObjectId -> throw IllegalArgumentException("Invalid object id.")
            ReturnCode.UnknownError -> throw Exception("Unknown error.")
            ReturnCode.FileError -> throw IOException("File operation error.")
            ReturnCode.TypeNotFound -> throw IllegalArgumentException("Type not found.")
            else -> {}
        }
    }
}