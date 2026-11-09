package com.panomc.plugins.countdowntimer.util

import java.io.File

/**
 * The background image is always stored as `bg-<timestamp>.<ext>` inside the plugin's uploads folder. Every route
 * that turns a stored or requested name into a file goes through [resolve], so a name with path segments can never
 * reach a file outside that folder.
 */
object BackgroundImageFiles {
    private val NAME_PATTERN = Regex("^bg-\\d+\\.(png|jpe?g|webp|gif)$", RegexOption.IGNORE_CASE)

    fun isValidName(name: String?): Boolean = name != null && NAME_PATTERN.matches(name)

    /** The file for [name] inside [uploadsDir], or null when the name is not a valid background name. */
    fun resolve(uploadsDir: File, name: String?): File? {
        if (!isValidName(name)) {
            return null
        }

        val base = uploadsDir.canonicalFile
        val file = File(base, name!!).canonicalFile

        if (file.parentFile != base) {
            return null
        }

        return file
    }
}
