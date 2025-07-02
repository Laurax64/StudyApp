package com.example.studyapp

import android.content.Context
import androidx.room.Room
import com.example.studyapp.data.AppDatabase
import com.example.studyapp.data.AppDatabase_Impl
import com.example.studyapp.data.SubtopicDao
import com.example.studyapp.data.SubtopicsRepositoryImpl
import com.example.studyapp.data.TopicDao
import com.example.studyapp.data.TopicsRepositoryImpl
import com.example.studyapp.di.Modules
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ModulesTest {

    @MockK
    private lateinit var appDatabase: AppDatabase_Impl

    @MockK
    private lateinit var topicDao: TopicDao

    @MockK
    private lateinit var subtopicDao: SubtopicDao
    private lateinit var topicsRepository: TopicsRepositoryImpl
    private lateinit var subtopicsRepository: SubtopicsRepositoryImpl
    private val modules = Modules

    @BeforeEach
    fun setUp() {
        topicsRepository = TopicsRepositoryImpl(topicDao = topicDao)
        subtopicsRepository = SubtopicsRepositoryImpl(subtopicDao = subtopicDao)
    }

    @Test
    fun testProvidesAppDatabase() {
        val context = mockk<Context>()
        coEvery { context.getSystemService(any()) } returns mockk()
        mockkStatic(Room::class)
        every {
            Room.databaseBuilder(context, AppDatabase::class.java, "app-database").build()
        } returns appDatabase


        assertEquals(appDatabase, modules.providesAppDatabase(context))
        verify { Room.databaseBuilder(context, AppDatabase::class.java, "app-database").build() }
    }

    @Test
    fun testProvideTopicDao() {
        every { appDatabase.topicDao() } returns topicDao
        assertEquals(topicDao, modules.provideTopicDao(appDatabase))
        verify { appDatabase.topicDao() }
    }

    @Test
    fun testProvideSubtopicDao() {
        every { appDatabase.subtopicDao() } returns subtopicDao
        assertEquals(subtopicDao, modules.provideSubtopicDao(appDatabase))
        verify { appDatabase.subtopicDao() }
    }

    @Test
    fun testProvideTopicsRepository() {
        val result = modules.provideTopicsRepository(topicDao = topicDao)
        assertTrue(result is TopicsRepositoryImpl && result.topicDao == topicDao)
    }

    @Test
    fun testProvideSubtopicsRepository() {
        val result = modules.provideSubtopicsRepository(subtopicDao = subtopicDao)
        assertTrue(result is SubtopicsRepositoryImpl && result.subtopicDao == subtopicDao)
    }
}