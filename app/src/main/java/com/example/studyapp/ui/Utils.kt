package com.example.studyapp.ui

import android.content.Context

/*
 * A collection of reusable functions, that perform common low-level tasks, and are designed to be
 * used across different parts of the ui.
 */

/**
 * Saved the given [fileContents] to the app specific storage.
 *
 * @param context The context
 * @param fileContents The contents of the file
 */
fun saveToAppSpecificStorage(context: Context, fileContents: ByteArray, filename: String) {
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(fileContents)
    }
}