package com.example.studyapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = TopicViewModel.Factory::class)
class TopicViewModel @AssistedInject constructor(
    topicsRepository: TopicsRepository,
    @Assisted val topicId: Int,
) : ViewModel() {
    val topicUiState: StateFlow<TopicUiState> = topicUiState(
        topicId = topicId,
        topicsRepository = topicsRepository,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TopicUiState.Loading,
    )


    @AssistedFactory
    interface Factory {
        fun create(
            topicId: Int,
        ): TopicViewModel
    }
}

private fun topicUiState(
    topicId: Int,
    topicsRepository: TopicsRepository,
): Flow<TopicUiState> {
    // Observe topic information
    val topicStream: Flow<Topic> = topicsRepository.getTopic(
        id = topicId,
    )
    // TODO: Handle error state and loading state
    return topicStream.map { topic ->
        TopicUiState.Success(topic = topic)
    }
}

sealed interface TopicUiState {
    data class Success(val topic: Topic) : TopicUiState
    data object Error : TopicUiState
    data object Loading : TopicUiState
}