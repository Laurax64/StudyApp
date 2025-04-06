package com.example.studyapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val topicsRepository: TopicsRepository,
) : ViewModel() {
    val topics: StateFlow<List<Topic>> =
        topicsRepository.getAllTopics()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = listOf<Topic>()
            )

    fun createTopic(title: String) {
        viewModelScope.launch {
            topicsRepository.insertTopic(Topic(title = title))
        }
    }

    suspend fun deleteTopic(topicToDelete: Topic) {
        topicsRepository.deleteTopic(topic = topicToDelete)
    }


}