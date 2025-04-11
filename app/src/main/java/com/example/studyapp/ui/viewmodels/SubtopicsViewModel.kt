package com.example.studyapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubtopicsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val topicsRepository: TopicsRepository,
) : ViewModel() {
    private val topicId: Int = savedStateHandle["topicId"] ?: -1
    var topic by mutableStateOf(
        Topic(title = "", checked = false)
    )
        private set

    init {
        viewModelScope.launch {
            topic = topicsRepository.getTopic(id = topicId)
                .filterNotNull()
                .first()
        }
    }

    suspend fun updateTopic(updatedTopic: Topic) {
        if (validateInput(updatedTopic)) {
            topicsRepository.updateTopic(updatedTopic)
        }
    }

    suspend fun deleteTopic() {
        topicsRepository.deleteTopic(topic)
    }

    suspend fun deleteSubtopic() {
        topicsRepository.deleteTopic(topic)
    }

    private fun validateInput(updatedTopic: Topic): Boolean {
        return with(updatedTopic) {
            topicId == id && title.isNotBlank()
        }
    }
}