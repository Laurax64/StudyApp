package com.example.studyapp

import com.example.studyapp.data.study.Subtopic
import com.example.studyapp.data.study.SubtopicDao
import com.example.studyapp.data.study.SubtopicsRepository
import com.example.studyapp.data.study.SubtopicsRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SubtopicsRepositoryTest {
    @MockK
    private lateinit var subtopicDao: SubtopicDao
    private lateinit var subtopicsRepository: SubtopicsRepository
    private val subtopic = Subtopic(
        title = "Test Subtopic",
        topicId = 1,
        id = 0,
        description = "Description",
        checked = false,
        bookmarked = false,
        imageUri = "imageUri"
    )

    @BeforeEach
    fun setup() {
        subtopicsRepository = SubtopicsRepositoryImpl(
            subtopicDao = subtopicDao
        )
    }

    @Test
    fun testInsertSubtopic() = runTest {
        coEvery { subtopicDao.insert(subtopic = subtopic) } returns Unit
        subtopicsRepository.insertSubtopic(subtopic = subtopic)
        coVerify { subtopicDao.insert(subtopic = subtopic) }
    }

    @Test
    fun testDeleteSubtopic() = runTest {
        coEvery { subtopicDao.delete(subtopicId = 0) } returns Unit
        subtopicsRepository.deleteSubtopic(subtopicId = 0)
        coVerify { subtopicDao.delete(subtopicId = 0) }
    }

    @Test
    fun testUpdateSubtopic() = runTest {
        coEvery { subtopicDao.update(subtopic = subtopic) } returns Unit
        subtopicsRepository.updateSubtopic(subtopic = subtopic)
        coVerify { subtopicDao.update(subtopic = subtopic) }
    }

    @Test
    fun testGetSubtopic() = runTest {
        val subtopicFlow = flowOf(subtopic)
        coEvery { subtopicDao.getSubtopic(subtopicId = 0) } returns subtopicFlow
        assertEquals(subtopicFlow, subtopicsRepository.getSubtopic(subtopicId = 0))
    }

    @Test
    fun testGetAllSubtopics() = runTest {
        val subtopicsFlow = flowOf(listOf(subtopic))
        coEvery { subtopicDao.getAllSubtopics() } returns subtopicsFlow
        assertEquals(subtopicsFlow, subtopicsRepository.getAllSubtopics())
    }

    @Test
    fun testDeleteAssociatedSubtopics() = runTest {
        coEvery { subtopicDao.deleteAssociatedSubtopics(topicId = 0) } returns Unit
        subtopicsRepository.deleteAssociatedSubtopics(topicId = 0)
        coVerify { subtopicDao.deleteAssociatedSubtopics(topicId = 0) }
    }

}