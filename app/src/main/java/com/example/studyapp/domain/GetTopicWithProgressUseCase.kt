package com.example.studyapp.domain

import com.example.studyapp.data.SubtopicsRepository
import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.data.TopicsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * A use case which obtains a list of topics which includes their associated progress.
 */
class GetTopicWithProgressUseCase @Inject constructor(
    private val topicsRepository: TopicsRepository,
    private val subtopicsRepository: SubtopicsRepository
) {
    /**
     * Returns a list of topics with their associated progress.
     *
     * @param id The id of the topic
     */
    operator fun invoke(id: Int): Flow<TopicWithProgress> = combine(
        topicsRepository.getTopic(id),
        subtopicsRepository.getAllSubtopics(id)
    ) { topic, subtopics ->
        if (topic == null) {
            throw Exception("Topic not found")
        } else
            TopicWithProgress(
                topic = topic,
                checked = subtopics.all { it.checked }
            )
    }
}