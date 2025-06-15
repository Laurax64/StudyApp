package com.example.studyapp.utils

import android.content.Context
import android.net.Uri

/**
 * Saves the content from the given [uri] to the app specific storage.
 *
 * Example:
 * ```
 * val inputUri = Uri.parse("content://media/picker/0/com.android.providers.media.photopicker/media/21")
 * val savedPath = saveToAppSpecificStorage(context, inputUri)
 * // savedPath is now: "/data/user/0/com.example.studyapp/files/21"
 * ```
 *
 * @param context The context to access the app specific storage
 * @param uri The uri of the content
 * @return The absolute file path of the content in the app specific storage
 * @throws IllegalArgumentException If the [uri] has no valid filename or the content cannot be read.
 */
fun saveToAppSpecificStorage(context: Context, uri: Uri): String {
    val filename = uri.lastPathSegment
        ?: throw IllegalArgumentException("Uri must have a valid last path segment as filename")
    val fileContents =
        context.contentResolver.openInputStream(uri)?.readBytes() ?: throw IllegalArgumentException(
            "Unable to read content from Uri"
        )
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(fileContents)
    }
    return context.filesDir.toString() + "/$filename"
}