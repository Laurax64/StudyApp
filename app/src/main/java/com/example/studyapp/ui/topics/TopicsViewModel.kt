package com.example.studyapp.ui.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicsRepository
import com.example.studyapp.domain.GetTopicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    getTopicsWithProgress: GetTopicsUseCase,
    private val topicsRepository: TopicsRepository
) : ViewModel() {
    val topics = getTopicsWithProgress().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    fun saveTopic(topic: Topic) {
        viewModelScope.launch {
            topicsRepository.insertTopic(topic = topic)
        }
    }
}

