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

    val uiState: StateFlow<SubtopicUiState> = combine(
            subtopicsRepository.getSubtopic(subtopicId = subtopicId),
            subtopicsRepository.getAllSubtopics()
        ) { subtopic, subtopics ->
            if (subtopic != null) {
                val index =
                    subtopics.indexOfFirst { it.id == subtopicId && it.topicId == subtopic.topicId }
                SubtopicUiState.Success(
                    subtopic = subtopic,
                    previousSubtopicId = subtopics.getOrNull(index - 1)?.id,
                    nextSubtopicId = subtopics.getOrNull(index + 1)?.id
                )
            } else if (subtopicId == -1) {
                SubtopicUiState.Error
            } else {
                SubtopicUiState.Loading
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SubtopicUiState.Loading
        )

    fun updateSubtopic(subtopic: Subtopic) {
        viewModelScope.launch {
            subtopicsRepository.updateSubtopic(subtopic = subtopic)
        }
    }

    fun deleteSubtopic() {
        viewModelScope.launch {
            subtopicsRepository.deleteSubtopic(subtopicId = subtopicId)
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

    object Error : SubtopicUiState
}


