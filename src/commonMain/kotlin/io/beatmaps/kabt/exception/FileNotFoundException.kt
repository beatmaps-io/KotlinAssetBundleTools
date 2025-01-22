package io.beatmaps.kabt.exception

class FileNotFoundException(message: String, file: String) : Exception("$message ($file)")
