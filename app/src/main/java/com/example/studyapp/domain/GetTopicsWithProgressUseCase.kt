package com.example.studyapp.domain

import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.data.TopicsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * A use case which obtains a list of topics which includes their associated progress.
 */
class GetTopicsWithProgressUseCase @Inject constructor(
    private val topicsRepository: TopicsRepository,
    private val getTopicWithProgressUseCase: GetTopicWithProgressUseCase,
) {
    /**
     * Returns a list of topics with their associated progress.
     *
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<TopicWithProgress>> =
        topicsRepository.getAllTopics()
            .flatMapLatest { topics ->
                val progressFlows = topics.map { topic ->
                    getTopicWithProgressUseCase(topic.id)
                }
                combine(progressFlows) { progressArray ->
                    progressArray.toList()
                }
            }

}