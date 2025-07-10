package com.example.studyapp.ui.study.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.study.Topic
import com.example.studyapp.data.study.TopicWithProgress
import com.example.studyapp.data.study.TopicsRepository
import com.example.studyapp.domain.study.GetTopicsWithProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    getTopicsWithProgressUseCase: GetTopicsWithProgressUseCase,
    private val topicsRepository: TopicsRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<TopicsUiState> = getTopicsWithProgressUseCase().map {
        TopicsUiState.Success(topicsWithProgress = it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TopicsUiState.Loading
    )

    internal fun addTopic(topic: Topic) {
        viewModelScope.launch {
            topicsRepository.insertTopic(topic = topic)
        }
    }


}

sealed interface TopicsUiState {
    object Loading : TopicsUiState
    data class Success(
        val topicsWithProgress: List<TopicWithProgress>,
    ) : TopicsUiState
}
