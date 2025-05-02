package com.example.studyapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicsRepository
import com.example.studyapp.navigation.TopicsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val topicsRepository: TopicsRepository
) : ViewModel() {

    // Key used to save and retrieve the currently selected topic id from saved state.
    private val selectedTopicIdKey = "selectedTopicIdKey"

    private val topicsRoute: TopicsRoute = savedStateHandle.toRoute()
    private val selectedTopicId = savedStateHandle.getStateFlow(
        key = selectedTopicIdKey,
        initialValue = topicsRoute.initialTopicId,
    )

    val uiState: StateFlow<TopicsUiState> = combine(
        selectedTopicId,
        topicsRepository.getAllTopics(),
        TopicsUiState::Topics,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TopicsUiState.Loading,
    )

    fun onTopicClick(topicId: Int?) {
        savedStateHandle[selectedTopicIdKey] = topicId
    }
}

sealed interface TopicsUiState {
    data object Loading : TopicsUiState

    data class Topics(
        val selectedTopicId: Int?,
        val topics: List<Topic>,
    ) : TopicsUiState

    data object Empty : TopicsUiState
}