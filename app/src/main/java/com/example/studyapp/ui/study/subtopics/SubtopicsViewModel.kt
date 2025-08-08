package com.example.studyapp.ui.study.subtopics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.authentication.UserPreferencesRepository
import com.example.studyapp.data.study.Subtopic
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
class SubtopicsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val topicsRepository: TopicsRepository,
    private val subtopicsRepository: SubtopicsRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val userId = userPreferencesRepository.userPreferencesFlow.map {
        it.userId
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    private val topicId: Int = savedStateHandle["topicId"] ?: -1
    val uiState: StateFlow<SubtopicsUiState> = combine(
        userPreferencesRepository.userPreferencesFlow,
        topicsRepository.getTopic(topicId),
        topicsRepository.getAllTopics(),
        subtopicsRepository.getAllSubtopics(),
    ) { userPreferences, selectedTopic, topics, subtopics ->
        val userId = userPreferences.userId
        if (selectedTopic != null && userId != null) {
            SubtopicsUiState.Success(
                selectedTopic = selectedTopic,
                topicsWithProgress = topics.filter { it.userId == userId }.map { topic ->
                    val topicSubtopics = subtopics.filter { it.topicId == topic.id }
                    TopicWithProgress(
                        topic = topic,
                        checked = topicSubtopics.all { it.checked }
                    )
                },
                subtopics = subtopics.filter { it.topicId == topicId },
            )
        } else {
            SubtopicsUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SubtopicsUiState.Loading
    )

    internal fun addSubtopic(subtopic: Subtopic) {
        viewModelScope.launch {
            userId.first()?.let {
                subtopicsRepository.insertSubtopic(subtopic.copy(userId = it))
            }
        }
    }

    internal fun updateTopic(updatedTopic: Topic) {
        viewModelScope.launch {
            userId.first()?.let {
                topicsRepository.updateTopic(topic = updatedTopic.copy(userId = it))
            }
        }
    }

    internal fun deleteTopic() {
        viewModelScope.launch {
            topicsRepository.deleteTopic(topicId = topicId)
            subtopicsRepository.deleteAssociatedSubtopics(topicId = topicId)
        }
    }
}

sealed interface SubtopicsUiState {
    object Loading : SubtopicsUiState
    data class Success(
        val selectedTopic: Topic,
        val topicsWithProgress: List<TopicWithProgress>,
        val subtopics: List<Subtopic>,
    ) : SubtopicsUiState
}
