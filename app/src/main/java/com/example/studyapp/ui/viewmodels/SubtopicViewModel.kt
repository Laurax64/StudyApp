package com.example.studyapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.SubtopicsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Could also use savedStateHandle.
@HiltViewModel(assistedFactory = SubtopicViewModel.Factory::class)
class SubtopicViewModel @AssistedInject constructor(
    private val subtopicsRepository: SubtopicsRepository,
    @Assisted val subtopicId: Int,
) : ViewModel() {
    var subtopic = subtopicsRepository.getSubtopic(
        id = subtopicId.toInt()
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    fun updateSubtopic(updatedSubtopic: Subtopic) {
        viewModelScope.launch {
            subtopicsRepository.updateSubtopic(subtopic = updatedSubtopic)
        }
    }

    fun deleteSubtopic() {
        viewModelScope.launch {
            subtopic.first()?.let {
                subtopicsRepository.deleteSubtopic(subtopic = it)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(subtopicId: Int): SubtopicViewModel
    }
}



