package com.example.studyapp

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.studyapp.utils.saveToAppSpecificStorage
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.io.path.createTempDirectory

class FileStorageUtilsTest {

    @Test
    fun testSaveToAppSpecificStorageSuccess() {
        val context = mockk<Context>()
        val contentResolver = mockk<ContentResolver>()
        val uri = mockk<Uri>()
        val testData = byteArrayOf(1, 2, 3)
        val filename = "testFile.txt"

        val tempDir = createTempDirectory().toFile()
        val expectedFile = File(tempDir, filename)

        every { uri.lastPathSegment } returns filename
        every { context.contentResolver } returns contentResolver
        every { contentResolver.openInputStream(uri) } returns ByteArrayInputStream(testData)
        every { context.filesDir } returns tempDir
        every { context.openFileOutput(filename, Context.MODE_PRIVATE) } answers {
            FileOutputStream(expectedFile)
        }

        val savedPath = saveToAppSpecificStorage(context, uri)

        assertTrue(File(savedPath).exists())
        assertArrayEquals(testData, File(savedPath).readBytes())

        // Cleanup
        tempDir.deleteRecursively()
    }

    @Test
    fun testSaveToAppSpecificStorageMissingFilename() {
        val context = mockk<Context>()
        val uri = mockk<Uri>()

        every { uri.lastPathSegment } returns null

        val exception = assertThrows(IllegalArgumentException::class.java) {
            saveToAppSpecificStorage(context, uri)
        }

        assertTrue(exception.message!!.contains("Uri must have a valid last path segment"))
    }

    @Test
    fun testSaveToAppSpecificStorageUnreadableContent() {
        val context = mockk<Context>()
        val uri = mockk<Uri>()
        val contentResolver = mockk<ContentResolver>()
        val filename = "unreadableFile.txt"

        every { uri.lastPathSegment } returns filename
        every { context.contentResolver } returns contentResolver
        every { contentResolver.openInputStream(uri) } returns null

        val exception = assertThrows(IllegalArgumentException::class.java) {
            saveToAppSpecificStorage(context, uri)
        }

        assertTrue(exception.message!!.contains("Unable to read content from Uri"))
    }
}
