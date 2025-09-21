package com.example.studyapp

import com.example.studyapp.data.study.Topic
import com.example.studyapp.data.study.TopicWithProgress
import com.example.studyapp.data.study.TopicsRepository
import com.example.studyapp.domain.study.GetTopicsWithProgressUseCase
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
    private lateinit var getTopicsWithProgressUseCase: GetTopicsWithProgressUseCase
    private lateinit var viewModel: TopicsViewModel
    private val topic = Topic(title = "Test Topic")
    private val topicsWithProgress: Flow<List<TopicWithProgress>> = flowOf(
        listOf(
            TopicWithProgress(
                topic = Topic(title = "Test Topic"),
                checked = true
            )
        )
    )

    @BeforeEach
    fun setup() {
        coEvery { topicsRepository.insertTopic(topic) } returns Unit
        coEvery { getTopicsWithProgressUseCase.invoke() } returns topicsWithProgress
        viewModel = TopicsViewModel(
            getTopicsWithProgressUseCase = getTopicsWithProgressUseCase,
            topicsRepository = topicsRepository
        )
    }

    @Test
    fun testAddTopic() = runTest {
        viewModel.addTopic(topic)
        coVerify { topicsRepository.insertTopic(topic) }
    }
}