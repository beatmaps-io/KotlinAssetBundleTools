package io.beatmaps.kabt.file

object CRC32 {
    private val poly = 0xedb88320u

    private val table =
        (0..255).map { idx ->
            (0..7).fold(idx.toUInt()) { temp, _ ->
                if ((temp and 1u) == 1u) {
                    ((temp shr 1) xor poly)
                } else {
                    temp shr 1
                }
            }
        }.toUIntArray()

    private val fast =
        (0..14).fold(table to table) { (last, fast), _ ->
            val newSlice = (0..255).map { idx ->
                (last[idx] shr 8) xor table[(last[idx] and 0xFFu).toInt()]
            }.toUIntArray()

            newSlice to fast.plus(newSlice)
        }.second

    fun calculateCRC32(data: UByteArray, initialCrc: UInt = 0u): UInt {
        val len = data.size

        fun fastIndex(offset: Int, index: Int, p: Int, crc: UInt = 0u): UInt =
            fast[offset + ((crc xor data[(p * 16) + index].toUInt()) and 0xFFu).toInt()]

        val iterations = len / 16
        val newCrc = (0..<iterations).fold(initialCrc.inv()) { prev, idx ->
            val num4 = fastIndex(768, 12, idx) xor fastIndex(512, 13, idx) xor fastIndex(256, 14, idx) xor fastIndex(0, 15, idx)
            val num3 = fastIndex(1792, 8, idx) xor fastIndex(1536, 9, idx) xor fastIndex(1280, 10, idx) xor fastIndex(1024, 11, idx)
            val num2 = fastIndex(2816, 4, idx) xor fastIndex(2560, 5, idx) xor fastIndex(2304, 6, idx) xor fastIndex(2048, 7, idx)
            val num1 = fastIndex(3840, 0, idx, prev) xor fastIndex(3584, 1, idx, prev shr 8) xor fastIndex(3328, 2, idx, prev shr 16) xor fastIndex(3072, 3, idx, prev shr 24)

            num1 xor num2 xor num3 xor num4
        }

        return (0..<(len % 16)).fold(newCrc) { prev, idx ->
            fastIndex(0, idx, iterations, prev) xor (prev shr 8)
        }.inv()
    }
}
