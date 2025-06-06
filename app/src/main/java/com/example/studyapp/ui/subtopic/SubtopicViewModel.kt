package com.example.studyapp.ui.subtopic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.SubtopicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubtopicViewModel @Inject constructor(
    private val subtopicsRepository: SubtopicsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val subtopicId: Int = savedStateHandle["subtopicId"] ?: -1
    private val topicId: Int = savedStateHandle["topicId"] ?: -1
    private val subtopics = subtopicsRepository.getAllSubtopics(topicId = topicId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val subtopic = subtopicsRepository.getSubtopic(id = subtopicId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

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

    fun updateSubtopic(updatedSubtopic: Subtopic) {
        viewModelScope.launch {
            subtopicsRepository.updateSubtopic(subtopic = updatedSubtopic)
        }
    }

    /**
     * Deletes the current subtopic and updates the index of all subtopics that come after it by
     * decreasing their index by 1.
     */
    fun deleteSubtopic() {
        val subtopics = subtopics.value ?: return
        val subtopic = subtopic.value ?: return
        val successors = subtopics.drop(subtopic.index + 1)
        viewModelScope.launch {
            successors.forEach { subtopicsRepository.updateSubtopic(it.copy(index = it.index - 1)) }
            subtopicsRepository.deleteSubtopic(subtopic = subtopic)
        }
    }
}

sealed interface SubtopicUiState {
    object Loading : SubtopicUiState
    data class Success(
        val subtopic: Subtopic,
        val previousSubtopicId: Int? = null,
        val nextSubtopicId: Int? = null
    ) : SubtopicUiState
}


