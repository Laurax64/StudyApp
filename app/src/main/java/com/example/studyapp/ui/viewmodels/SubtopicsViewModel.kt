package com.example.studyapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.SubtopicsRepository
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubtopicsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val topicsRepository: TopicsRepository,
    private val subtopicsRepository: SubtopicsRepository,
) : ViewModel() {
    private val topicId: Int = savedStateHandle["topicId"] ?: -1
    val topic = topicsRepository.getTopic(id = topicId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    val subtopics = subtopicsRepository.getAllSubtopics(topicId = topicId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    val topics = topicsRepository.getAllTopics()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
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
                    topicId = topicId
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
                topicsRepository.deleteTopic(topic = it)
            }
        }
    }
}