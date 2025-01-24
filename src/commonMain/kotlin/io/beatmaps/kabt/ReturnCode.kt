package io.beatmaps.kabt

enum class ReturnCode(val code: UInt) {
    Success(0u),
    AlreadyInitialized(1u),
    NotInitialized(2u),
    FileNotFound(3u),
    FileFormatError(4u),
    InvalidArgument(5u),
    HigherSerializedFileVersion(6u),
    DestinationBufferTooSmall(7u),
    InvalidObjectId(8u),
    UnknownError(9u),
    FileError(10u),
    ErrorCreatingArchiveFile(11u),
    ErrorAddingFileToArchive(12u),
    TypeNotFound(13u);

    companion object {
        private val map = entries.associateBy { it.code }
        fun fromInt(code: UInt) = map.getValue(code)
    }
}
