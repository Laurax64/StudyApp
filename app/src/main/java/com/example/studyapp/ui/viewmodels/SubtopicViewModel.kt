package com.example.studyapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.SubtopicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class SubtopicViewModel @Inject constructor(
    private val subtopicsRepository: SubtopicsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val subtopicId: Int = savedStateHandle["subtopicId"] ?: -1
    var subtopic = subtopicsRepository.getSubtopic(
        id = subtopicId
    )

    fun updateSubtopic(updatedSubtopic: Subtopic) {
        viewModelScope.launch {
            subtopicsRepository.updateSubtopic(subtopic = updatedSubtopic)
        }
    }

    fun deleteSubtopic() {
        viewModelScope.launch {
            subtopicsRepository.deleteSubtopic(subtopic = subtopic.first())
        }
    }
}



