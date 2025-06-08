package com.example.studyapp.ui.subtopics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.SubtopicsRepository
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.data.TopicsRepository
import com.example.studyapp.domain.GetTopicWithProgressUseCase
import com.example.studyapp.domain.GetTopicsWithProgressUseCase
import com.example.studyapp.ui.subtopic.SubtopicUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubtopicsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTopicWithProgressUseCase: GetTopicWithProgressUseCase,
    getTopicsWithProgressUseCase: GetTopicsWithProgressUseCase,
    private val topicsRepository: TopicsRepository,
    private val subtopicsRepository: SubtopicsRepository,
) : ViewModel() {
    private val topicId: Int = savedStateHandle["topicId"] ?: -1

    val uiState: StateFlow<SubtopicUiState> = subtopic.combine(subtopics) { subtopic, subtopics ->
        if (subtopics != null && subtopic != null) {
            val index = subtopics.indexOfFirst { it.id == subtopicId }
            SubtopicUiState.Success(
                subtopic = subtopic,
                previousSubtopicId = subtopics.getOrNull(index - 1)?.id,
                nextSubtopicId = subtopics.getOrNull(index + 1)?.id
            )
        } else {
            SubtopicUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SubtopicUiState.Loading
    )


    val subtopics = subtopicsRepository.getAllSubtopics(topicId = topicId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5_000),
            initialValue = null
        )

    val topics = getTopicsWithProgressUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5_000),
            initialValue = null
        )

    val topic: StateFlow<TopicWithProgress?> = getTopicWithProgressUseCase(id = topicId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = null
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
            topic.first()?.let {
                topicsRepository.deleteTopic(id = it.topic.id)
            }
        }
    }
}

sealed interface SubtopicsUiState {
    object Loading : SubtopicsUiState
    data class Success(
        val subtopics: List<Subtopic>,
        val topic: TopicWithProgress
    ) : SubtopicsUiState
}