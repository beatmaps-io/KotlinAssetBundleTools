package io.beatmaps.kabt

enum class ReturnCode(val code: Int) {
    Success(0),
    AlreadyInitialized(1),
    NotInitialized(2),
    FileNotFound(3),
    FileFormatError(4),
    InvalidArgument(5),
    HigherSerializedFileVersion(6),
    DestinationBufferTooSmall(7),
    InvalidObjectId(8),
    UnknownError(9),
    FileError(10),
    ErrorCreatingArchiveFile(11),
    ErrorAddingFileToArchive(12),
    TypeNotFound(13);

    companion object {
        private val map = entries.associateBy { it.code }
        fun fromInt(code: Int) = map.getValue(code)
    }
}
