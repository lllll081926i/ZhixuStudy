package com.example.data.parser

object PinyinMatcher {

    /**
     * Converts a Chinese character into its initial Pinyin letter ('a'..'z').
     * Uses standard GB2312 section-position encoding range mapping.
     */
    fun getCharInitial(c: Char): Char {
        if (c in 'a'..'z' || c in 'A'..'Z') return c.lowercaseChar()
        if (c in '0'..'9') return c

        val bytes = try {
            c.toString().toByteArray(charset("GB2312"))
        } catch (e: Exception) {
            return c
        }

        if (bytes.size < 2) return c

        // GB2312 high-byte and low-byte to integer code
        val code = ((bytes[0].toInt() and 0xFF) shl 8) or (bytes[1].toInt() and 0xFF)

        return when (code) {
            in 0xB0A1..0xB0C4 -> 'a'
            in 0xB0C5..0xB2C0 -> 'b'
            in 0xB2C1..0xB4ED -> 'c'
            in 0xB4EE..0xB6E9 -> 'd'
            in 0xB6EA..0xB7A1 -> 'e'
            in 0xB7A2..0xB8C0 -> 'f'
            in 0xB8C1..0xB9FD -> 'g'
            in 0xB9FE..0xBBF6 -> 'h'
            in 0xBBF7..0xBFA5 -> 'j'
            in 0xBFA6..0xC0AB -> 'k'
            in 0xC0AC..0xC2E7 -> 'l'
            in 0xC2E8..0xC4C2 -> 'm'
            in 0xC4C3..0xC5B5 -> 'n'
            in 0xC5B6..0xC5BD -> 'o'
            in 0xC5BE..0xC6D9 -> 'p'
            in 0xC6DA..0xC8BA -> 'q'
            in 0xC8BB..0xC8F5 -> 'r'
            in 0xC8F6..0xCBF9 -> 's'
            in 0xCBFA..0xCDD9 -> 't'
            in 0xCDDA..0xCEF3 -> 'w'
            in 0xCEF4..0xD1B8 -> 'x'
            in 0xD1B9..0xD4D0 -> 'y'
            in 0xD4D1..0xD7F9 -> 'z'
            else -> c
        }
    }

    /**
     * Returns the Pinyin initials string for a given Chinese text (e.g., "铁路运输" -> "tlys").
     */
    fun getInitials(text: String): String {
        val sb = StringBuilder()
        for (c in text) {
            val initial = getCharInitial(c)
            if (initial in 'a'..'z' || initial in '0'..'9') {
                sb.append(initial)
            }
        }
        return sb.toString()
    }

    /**
     * Checks whether [target] matches [query] by direct text, lower-case, or Pinyin initials.
     */
    fun matches(target: String, query: String): Boolean {
        val q = query.trim()
        if (q.isEmpty()) return true
        if (target.contains(q, ignoreCase = true)) return true

        val initials = getInitials(target)
        return initials.contains(q.lowercase())
    }
}
