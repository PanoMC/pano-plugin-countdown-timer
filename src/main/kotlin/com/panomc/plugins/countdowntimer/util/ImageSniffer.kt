package com.panomc.plugins.countdowntimer.util

import java.io.File

/**
 * Detects the image type from the file's first bytes. The client's Content-Type and file name are never trusted,
 * so a file that is not really a PNG, JPEG, GIF or WebP image gets no extension and is rejected.
 */
object ImageSniffer {
    fun detectExtension(file: File): String? {
        val header = ByteArray(12)
        val read = file.inputStream().use { it.read(header) }

        if (read < 12) {
            return null
        }

        fun at(index: Int, vararg bytes: Int) = bytes.withIndex().all { (i, b) -> header[index + i] == b.toByte() }

        return when {
            at(0, 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A) -> "png"
            at(0, 0xFF, 0xD8, 0xFF) -> "jpg"
            at(0, 0x47, 0x49, 0x46, 0x38) -> "gif"
            at(0, 0x52, 0x49, 0x46, 0x46) && at(8, 0x57, 0x45, 0x42, 0x50) -> "webp"
            else -> null
        }
    }

    fun mimeTypeOf(extension: String): String? = when (extension.lowercase()) {
        "png" -> "image/png"
        "jpg", "jpeg" -> "image/jpeg"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        else -> null
    }
}
