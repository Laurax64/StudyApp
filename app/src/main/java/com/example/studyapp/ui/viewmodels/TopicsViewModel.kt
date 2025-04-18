package com.example.studyapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val topicsRepository: TopicsRepository,
) : ViewModel() {
    val topics =
        topicsRepository.getAllTopics()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null
            )


    fun createTopic(title: String) {
        viewModelScope.launch {
            topicsRepository.insertTopic(Topic(title = title, checked = false))
        }
    }

    fun deleteTopic(topic: Topic) {
        viewModelScope.launch {
            topicsRepository.deleteTopic(topic = topic)
        }
    }

    fun updateChecked(topic: Topic, checked: Boolean) {
        viewModelScope.launch {
            topicsRepository.updateTopic(
                topic = topic.copy(checked = checked)
            )
        }
    }

    fun updateTopic(topic: Topic) {
        viewModelScope.launch {
            topicsRepository.updateTopic(topic = topic)
        }
    }
}