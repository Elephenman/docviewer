package com.elephenman.docviewer.util

import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetDecoder
import java.nio.charset.CodingErrorAction
import java.nio.charset.StandardCharsets

object EncodingDetector {

    private val COMMON_ENCODINGS = listOf(
        StandardCharsets.UTF_8,
        StandardCharsets.UTF_16,
        StandardCharsets.UTF_16BE,
        StandardCharsets.UTF_16LE,
        Charset.forName("GBK"),
        Charset.forName("GB2312"),
        Charset.forName("BIG5"),
        Charset.forName("Shift_JIS"),
        Charset.forName("EUC-JP"),
        Charset.forName("ISO-8859-1"),
        Charset.forName("Windows-1252")
    )

    fun detect(bytes: ByteArray): Charset {
        if (bytes.isEmpty()) return StandardCharsets.UTF_8

        // BOM detection
        detectBOM(bytes)?.let { return it }

        // Try UTF-8 first (most common)
        if (isValidUtf8(bytes)) {
            return StandardCharsets.UTF_8
        }

        // Score each encoding
        var bestEncoding = StandardCharsets.UTF_8
        var bestScore = -1.0

        for (charset in COMMON_ENCODINGS) {
            val score = scoreEncoding(bytes, charset)
            if (score > bestScore) {
                bestScore = score
                bestEncoding = charset
            }
        }

        return bestEncoding
    }

    private fun detectBOM(bytes: ByteArray): Charset? {
        if (bytes.size >= 3 && bytes[0] == 0xEF.toByte() && bytes[1] == 0xBB.toByte() && bytes[2] == 0xBF.toByte()) {
            return StandardCharsets.UTF_8
        }
        if (bytes.size >= 2 && bytes[0] == 0xFE.toByte() && bytes[1] == 0xFF.toByte()) {
            return StandardCharsets.UTF_16BE
        }
        if (bytes.size >= 2 && bytes[0] == 0xFF.toByte() && bytes[1] == 0xFE.toByte()) {
            return StandardCharsets.UTF_16LE
        }
        return null
    }

    private fun isValidUtf8(bytes: ByteArray): Boolean {
        var i = 0
        while (i < bytes.size) {
            val b = bytes[i].toInt() and 0xFF
            when {
                b < 0x80 -> i++
                b < 0xC0 -> return false
                b < 0xE0 -> {
                    if (i + 1 >= bytes.size) return false
                    val b2 = bytes[i + 1].toInt() and 0xFF
                    if (b2 < 0x80 || b2 >= 0xC0) return false
                    i += 2
                }
                b < 0xF0 -> {
                    if (i + 2 >= bytes.size) return false
                    val b2 = bytes[i + 1].toInt() and 0xFF
                    val b3 = bytes[i + 2].toInt() and 0xFF
                    if (b2 < 0x80 || b2 >= 0xC0 || b3 < 0x80 || b3 >= 0xC0) return false
                    i += 3
                }
                b < 0xF8 -> {
                    if (i + 3 >= bytes.size) return false
                    val b2 = bytes[i + 1].toInt() and 0xFF
                    val b3 = bytes[i + 2].toInt() and 0xFF
                    val b4 = bytes[i + 3].toInt() and 0xFF
                    if (b2 < 0x80 || b2 >= 0xC0 || b3 < 0x80 || b3 >= 0xC0 || b4 < 0x80 || b4 >= 0xC0) return false
                    i += 4
                }
                else -> return false
            }
        }
        return true
    }

    private fun scoreEncoding(bytes: ByteArray, charset: Charset): Double {
        return try {
            val decoder: CharsetDecoder = charset.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
            val buffer = ByteBuffer.wrap(bytes)
            decoder.decode(buffer)

            // If it decodes successfully, score based on character distribution
            val str = String(bytes, charset)
            var score = 1.0

            // Prefer encodings with more printable characters
            val printableCount = str.count { it.isWhitespace() || it.isLetterOrDigit() || ".,;:!?-_'\"()[]{}<>@#\$%&*+/=~`|\\".contains(it) }
            val ratio = printableCount.toDouble() / str.length
            score *= ratio

            // Penalize encodings that produce many replacement characters
            val replacementCount = str.count { it == '�' }
            if (replacementCount > 0) {
                score *= (1.0 - replacementCount.toDouble() / str.length)
            }

            score
        } catch (_: Exception) {
            -1.0
        }
    }

    fun getAllEncodings(): List<String> {
        return COMMON_ENCODINGS.map { it.name() }
    }
}
