package com.example.studyapp

import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.SubtopicsRepository
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.data.TopicsRepository
import com.example.studyapp.domain.GetTopicsWithProgressUseCase
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetTopicsWithProgressUseCaseTest {
    @MockK
    private lateinit var topicsRepository: TopicsRepository

    @MockK
    private lateinit var subtopicsRepository: SubtopicsRepository


    @Test
    fun testInvoke() = runTest {
        val topic1 = Topic(id = 0, title = "Test Topic")
        val topic2 = Topic(id = 1, title = "Test Topic")
        val subtopic1 = Subtopic(
            id = 0, title = "Test Subtopic", topicId = 0,
            description = "Description 1",
            checked = false,
            bookmarked = false,
            imageUri = ""
        )
        val subtopic2 = Subtopic(
            id = 1, title = "Test Subtopic", topicId = 1,
            description = "Description 2",
            checked = true,
            bookmarked = false,
            imageUri = null
        )
        val topicsFlow = flowOf(listOf(topic1, topic2))
        val subtopicsFlow = flowOf(listOf(subtopic1, subtopic2))

        coEvery { topicsRepository.getAllTopics() } returns topicsFlow
        coEvery { subtopicsRepository.getAllSubtopics() } returns subtopicsFlow

        assertEquals(
                listOf(
                    TopicWithProgress(topic = topic1, checked = false),
                    TopicWithProgress(topic = topic2, checked = true)
            ),
            GetTopicsWithProgressUseCase(
                topicsRepository = topicsRepository,
                subtopicsRepository = subtopicsRepository
            ).invoke().first()
        )
    }
}