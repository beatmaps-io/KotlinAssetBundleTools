import kotlinx.cinterop.addressOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.getcwd

actual fun getCwd(): String? {
    return ByteArray(1024).usePinned { getcwd(it.addressOf(0), 1024u) }?.toKString()
}
