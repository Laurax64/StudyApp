package com.example.studyapp

import androidx.lifecycle.SavedStateHandle
import com.example.studyapp.data.authentication.UserPreferencesRepository
import com.example.studyapp.data.study.Subtopic
import com.example.studyapp.data.study.SubtopicsRepository
import com.example.studyapp.data.study.Topic
import com.example.studyapp.data.study.TopicsRepository
import com.example.studyapp.ui.study.subtopics.SubtopicsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SubtopicsViewModelTest {
    @MockK
    private lateinit var subtopicsRepository: SubtopicsRepository

    @MockK
    private lateinit var topicsRepository: TopicsRepository

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    private lateinit var viewModel: SubtopicsViewModel
    private val topic = Topic(id = 0, title = "Test Topic")
    private val subtopic = Subtopic(
        title = "Test Subtopic",
        topicId = 1,
        id = 0,
        description = "Description",
        checked = false,
        bookmarked = false,
        imageUri = "imageUri"
    )
    private val topics: Flow<List<Topic>> = flowOf(
        listOf(
            Topic(title = "Test Topic"),
        )
    )

    @BeforeEach
    fun setup() {
        coEvery { topicsRepository.getAllTopics() } returns topics
        coEvery { userPreferencesRepository.userPreferencesFlow } returns flowOf(
            com.example.studyapp.data.authentication.UserPreferences(
                userId = "Example@gmail.com"
            )
        )
        coEvery { topicsRepository.updateTopic(topic = any()) } returns Unit
        coEvery { topicsRepository.deleteTopic(topicId = any()) } returns Unit
        coEvery { topicsRepository.getTopic(0) } returns flowOf(topic)
        coEvery { subtopicsRepository.insertSubtopic(subtopic = any()) } returns Unit
        coEvery { subtopicsRepository.deleteAssociatedSubtopics(topicId = any()) } returns Unit
        coEvery { subtopicsRepository.getAllSubtopics() } returns flowOf(listOf(subtopic))
        every<Int?> { savedStateHandle["topicId"] } returns 0
        viewModel = SubtopicsViewModel(
            subtopicsRepository = subtopicsRepository,
            topicsRepository = topicsRepository,
            userPreferencesRepository = userPreferencesRepository,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun testAddSubtopic() = runTest {
        viewModel.addSubtopic(subtopic = subtopic)
        coVerify { subtopicsRepository.insertSubtopic(subtopic = subtopic) }
    }

    @Test
    fun testUpdateTopic() = runTest {
        val newTopic = topic.copy(title = "New Title")
        viewModel.updateTopic(newTopic)
        coVerify { topicsRepository.updateTopic(topic = newTopic) }
    }

    @Test
    fun testDeleteTopic() = runTest {
        viewModel.deleteTopic()
        coVerify { topicsRepository.deleteTopic(topicId = 0) }
        coVerify { subtopicsRepository.deleteAssociatedSubtopics(topicId = 0) }
    }
}