package com.example.studyapp

import androidx.lifecycle.SavedStateHandle
import com.example.studyapp.data.study.Subtopic
import com.example.studyapp.data.study.SubtopicsRepository
import com.example.studyapp.ui.study.subtopic.SubtopicViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SubtopicViewModelTest {
    @MockK
    private lateinit var subtopicsRepository: SubtopicsRepository
    @MockK
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: SubtopicViewModel
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
        coEvery { subtopicsRepository.deleteSubtopic(any()) } returns Unit
        coEvery { subtopicsRepository.updateSubtopic(any()) } returns Unit
        coEvery { subtopicsRepository.getAllSubtopics() } returns flowOf(listOf(subtopic))
        coEvery { subtopicsRepository.getSubtopic(0) } returns flowOf(subtopic)
        every<Int?> { savedStateHandle["subtopicId"] } returns 0
        viewModel = SubtopicViewModel(
            subtopicsRepository = subtopicsRepository,
            savedStateHandle = savedStateHandle
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testUpdateSubtopic() = runTest {
        val newSubtopic = subtopic.copy(title = "New Title")
        viewModel.updateSubtopic(newSubtopic)
        advanceUntilIdle()
        coVerify { subtopicsRepository.updateSubtopic(subtopic = newSubtopic) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDeleteSubtopic() = runTest {
        viewModel.deleteSubtopic()
        advanceUntilIdle()
        coVerify { subtopicsRepository.deleteSubtopic(subtopicId = 0) }
    }

}