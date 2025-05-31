package com.example.studyapp.ui.viewmodels

import android.util.Log
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
    val topics = topicsRepository.getAllTopics()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun saveTopic(topic: Topic) {
        viewModelScope.launch {
            Log.d("TopicsViewModel", "Saving topic: $topic")
            topicsRepository.insertTopic(topic = topic)
            Log.d("TopicsViewModel", "Topic saved")
        }
    }
}