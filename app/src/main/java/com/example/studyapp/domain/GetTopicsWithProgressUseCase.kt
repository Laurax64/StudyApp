package com.example.studyapp.domain

import com.example.studyapp.data.SubtopicsRepository
import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.data.TopicsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * A use case which obtains a list of topics which includes their associated progress.
 */
class GetTopicsWithProgressUseCase @Inject constructor(
    private val topicsRepository: TopicsRepository,
    private val subtopicsRepository: SubtopicsRepository
) {
    /**
     * Returns a list of topics with their associated progress.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<TopicWithProgress>> =
        combine(
            topicsRepository.getAllTopics(),
            subtopicsRepository.getAllSubtopics()
        ) { topics, subtopics ->
            topics.map { topic ->
                TopicWithProgress(
                    topic = topic,
                    checked = subtopics.all { it.checked && it.topicId == topic.id }
                )
            }
        }

}