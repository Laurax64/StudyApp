package com.example.studyapp.ui.subtopics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.SubtopicsRepository
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.data.TopicsRepository
import com.example.studyapp.domain.GetTopicsWithProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubtopicsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTopicsWithProgressUseCase: GetTopicsWithProgressUseCase,
    private val topicsRepository: TopicsRepository,
    private val subtopicsRepository: SubtopicsRepository,
) : ViewModel() {
    private val topicId: Int = savedStateHandle["topicId"] ?: -1

    val uiState: StateFlow<SubtopicsUiState> = combine(
        getTopicsWithProgressUseCase(),
        subtopicsRepository.getAllSubtopics(topicId = topicId)
    ) { topics, subtopics ->
        val selectedTopic = topics.find { it.topic.id == topicId }?.topic
        if (selectedTopic != null) {
            SubtopicsUiState.Success(
                selectedTopic = selectedTopic,
                topicsWithProgress = topics,
                subtopics = subtopics
            )
        } else {
            SubtopicsUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SubtopicsUiState.Loading
    )

    fun createSubtopic(
        title: String,
        description: String,
        imageUri: String?
    ) {
        viewModelScope.launch {
            subtopicsRepository.insertSubtopic(
                Subtopic(
                    title = title,
                    checked = false,
                    bookmarked = false,
                    description = description,
                    imageUri = imageUri,
                    topicId = topicId,
                )
            )
        }
    }

    fun updateTopic(updatedTopic: Topic) {
        viewModelScope.launch {
            topicsRepository.updateTopic(topic = updatedTopic)
        }
    }

    fun deleteTopic() {
        viewModelScope.launch {
            topicsRepository.deleteTopic(id = topicId)
        }
    }
}

sealed interface SubtopicsUiState {
    object Loading : SubtopicsUiState
    data class Success(
        val selectedTopic: Topic,
        val topicsWithProgress: List<TopicWithProgress>,
        val subtopics: List<Subtopic>
    ) : SubtopicsUiState
}
