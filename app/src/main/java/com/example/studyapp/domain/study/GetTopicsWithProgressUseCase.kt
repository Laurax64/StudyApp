package com.example.studyapp.domain.study

import com.example.studyapp.data.study.SubtopicsRepository
import com.example.studyapp.data.study.TopicWithProgress
import com.example.studyapp.data.study.TopicsRepository
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
    operator fun invoke(): Flow<List<TopicWithProgress>> =
        combine(
            topicsRepository.getAllTopics(),
            subtopicsRepository.getAllSubtopics()
        ) { topics, subtopics ->
            val subtopicsByTopic = subtopics.groupBy { it.topicId }
            topics.map { topic ->
                val topicSubtopics = subtopicsByTopic[topic.id].orEmpty()
                TopicWithProgress(
                    topic = topic,
                    checked = topicSubtopics.all { it.checked }
                )
            }
        }
}