package com.example.studyapp.ui.study.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.authentication.UserPreferencesRepository
import com.example.studyapp.data.study.SubtopicsRepository
import com.example.studyapp.data.study.Topic
import com.example.studyapp.data.study.TopicWithProgress
import com.example.studyapp.data.study.TopicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val topicsRepository: TopicsRepository,
    subtopicsRepository: SubtopicsRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val userId = userPreferencesRepository.userPreferencesFlow.map {
        it.userId
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    val uiState: StateFlow<TopicsUiState> =
        combine(
            userPreferencesRepository.userPreferencesFlow,
            topicsRepository.getAllTopics(),
            subtopicsRepository.getAllSubtopics(),

            ) { userPreferences, topics, subtopics ->
            val userId = userPreferences.userId
            TopicsUiState.Success(
                topicsWithProgress = topics.filter { it.userId == userId }.map { topic ->
                    val topicSubtopics = subtopics.filter { it.topicId == topic.id }
                    TopicWithProgress(
                        topic = topic,
                        checked = topicSubtopics.all { it.checked }
                    )
                }
            )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TopicsUiState.Loading
    )

    internal fun addTopic(topic: Topic) {
        viewModelScope.launch {
            userId.first()?.let {
                topicsRepository.insertTopic(topic = topic.copy(userId = it))
            }
        }
    }
}

sealed interface TopicsUiState {
    object Loading : TopicsUiState
    data class Success(
        val topicsWithProgress: List<TopicWithProgress>,
    ) : TopicsUiState
}
