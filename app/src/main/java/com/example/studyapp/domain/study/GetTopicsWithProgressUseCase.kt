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
     * Returns a list of topics with their associated progress. When a user is logged in, the list
     * is filtered by the user ID. Otherwise, the list is not filtered since the app does not require
     * users to sign in.
     *
     * @param userId The user ID of the current user or null if no user is logged in.
     */
    operator fun invoke(userId: Flow<String?>): Flow<List<TopicWithProgress>> =
        combine(
            userId,
            topicsRepository.getAllTopics(),
            subtopicsRepository.getAllSubtopics(),
        ) { userId, topics, subtopics ->
            val subtopicsByTopic = subtopics.filter { it.userId == userId }.groupBy { it.topicId }
            topics.filter { it.userId == userId }.map { topic ->
                val topicSubtopics = subtopicsByTopic[topic.id].orEmpty()
                TopicWithProgress(
                    topic = topic,
                    checked = topicSubtopics.all { it.checked }
                )
            }
        }
}