package com.example.studyapp

import com.example.studyapp.data.authentication.UserPreferencesRepository
import com.example.studyapp.data.study.SubtopicsRepository
import com.example.studyapp.data.study.Topic
import com.example.studyapp.data.study.TopicsRepository
import com.example.studyapp.ui.study.topics.TopicsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TopicsViewModelTest {
    @MockK
    private lateinit var topicsRepository: TopicsRepository
    @MockK
    private lateinit var subtopicsRepository: SubtopicsRepository

    @MockK
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    private lateinit var viewModel: TopicsViewModel
    private val topic = Topic(title = "Test Topic")
    private val topics: Flow<List<Topic>> = flowOf(
        listOf(
            Topic(title = "Test Topic"),
        )
    )


    @BeforeEach
    fun setup() {
        coEvery { subtopicsRepository.getAllSubtopics() } returns flowOf(emptyList())
        coEvery { topicsRepository.getAllTopics() } returns topics
        coEvery { topicsRepository.insertTopic(topic) } returns Unit
        coEvery { userPreferencesRepository.userPreferencesFlow } returns flowOf(
            com.example.studyapp.data.authentication.UserPreferences(
                userId = "Example@gmail.com"
            )
        )

        viewModel = TopicsViewModel(
            topicsRepository = topicsRepository,
            subtopicsRepository = subtopicsRepository,
            userPreferencesRepository = userPreferencesRepository
        )
    }

    @Test
    fun testAddTopic() = runTest {
        viewModel.addTopic(topic)
        coVerify { topicsRepository.insertTopic(topic) }
    }
}