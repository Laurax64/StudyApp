package com.example.studyapp.domain

import com.example.studyapp.data.SubtopicsRepository
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

/**
 * A use case which obtains a list of topics which includes their associated progress.
 */
class GetTopicsUseCase @Inject constructor(
    private val topicsRepository: TopicsRepository,
    private val subtopicsRepository: SubtopicsRepository
) {
    /**
     * Returns a list of topics with their associated progress.
     */
    operator fun invoke(): Flow<List<Topic>> =
        topicsRepository.getAllTopics().transform { topics ->
            topics.map { topic ->
                val subtopics = subtopicsRepository.getAllSubtopics(topic.id)
                val checked = subtopics.first().all { it.checked }
                topic.copy(checked = checked)
            }
        }
}

