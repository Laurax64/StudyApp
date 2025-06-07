package com.example.studyapp.domain

import com.example.studyapp.data.SubtopicsRepository
import com.example.studyapp.data.TopicsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteTopicUseCase @Inject constructor(
    private val topicsRepository: TopicsRepository,
    private val subtopicsRepository: SubtopicsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    /**
     * Deletes a topic and its associated subtopics.
     *
     * @param id The id of the topic to delete
     */
    suspend operator fun invoke(id: Int) =
        withContext(defaultDispatcher) {
            topicsRepository.deleteTopic(id = id)
            subtopicsRepository.deleteAssociatedSubtopics(topicId = id)
        }
}