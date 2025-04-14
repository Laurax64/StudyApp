package com.example.studyapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.SubtopicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubtopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val subtopicsRepository: SubtopicsRepository
) : ViewModel() {
    private val subtopicId: Int = savedStateHandle["subtopicId"] ?: -1
    var subtopic by mutableStateOf(
        Subtopic(
            title = "",
            description = "",
            checked = false,
            imageUri = null
        )
    )
        private set

    init {
        viewModelScope.launch {
            subtopic = subtopicsRepository.getSubtopic(id = subtopicId)
                .filterNotNull()
                .first()
        }
    }

    suspend fun updateSubtopic(updatedSubtopic: Subtopic) {
        if (validateInput(updatedSubtopic)) {
            subtopicsRepository.updateSubtopic(updatedSubtopic)
        }
    }


    suspend fun deleteSubtopic() {
        subtopicsRepository.deleteSubtopic(subtopic)
    }

    private fun validateInput(updatedSubtopic: Subtopic): Boolean {
        return with(updatedSubtopic) {
            subtopicId == id && title.isNotBlank() && description.isNotBlank()
        }
    }
}



