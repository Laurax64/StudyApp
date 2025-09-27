package com.example.studyapp

import com.example.studyapp.data.study.Topic
import com.example.studyapp.data.study.TopicDao
import com.example.studyapp.data.study.TopicsRepository
import com.example.studyapp.data.study.TopicsRepositoryImpl
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
class TopicsRepositoryTest {
    @MockK
    private lateinit var topicDao: TopicDao
    private lateinit var topicsRepository: TopicsRepository
    private val topic1 = Topic(id = 0, title = "Test Topic")
    private val topic2 = //Topic(id = 1, title = "Test Topic")
    private val topicsFlow = flowOf(listOf(topic1, topic2))

    @BeforeEach
    fun setup() {
        topicsRepository = TopicsRepositoryImpl(
            topicDao = topicDao
        )
    }

    @Test
    fun testInsertTopic() = runTest {
        coEvery { topicDao.insert(topic = topic1) } returns Unit
        topicsRepository.insertTopic(topic = topic1)
        coVerify { topicDao.insert(topic = topic1) }
    }

    @Test
    fun testDeleteTopic() = runTest {
        coEvery { topicDao.delete(topicId = 0) } returns Unit
        topicsRepository.deleteTopic(topicId = 0)
        coVerify { topicDao.delete(topicId = 0) }
    }

    @Test
    fun testUpdateTopic() = runTest {
        coEvery { topicDao.update(topic = topic1) } returns Unit
        topicsRepository.updateTopic(topic = topic1)
        coVerify { topicDao.update(topic = topic1) }
    }

    @Test
    fun testGetTopic() = runTest {
        val topic1Flow = flowOf(topic1)
        coEvery { topicDao.getTopic(topicId = 0) } returns topic1Flow
        assertEquals(topic1Flow, topicsRepository.getTopic(id = 0))
    }

    @Test
    fun testGetAllTopics() = runTest {
        coEvery { topicDao.getAllTopics() } returns topicsFlow
        assertEquals(topicsFlow, topicsRepository.getAllTopics())
    }
}