package com.example.studyapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.studyapp.navigation.TopicsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


const val TOPIC_ID_KEY = "selectedTopicId"

@HiltViewModel
class Topics2PaneViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val route = savedStateHandle.toRoute<TopicsRoute>()
    val selectedTopicId: StateFlow<Int?> = savedStateHandle.getStateFlow(
        key = TOPIC_ID_KEY,
        initialValue = route.initialTopicId,
    )

    fun onTopicClick(topicId: Int?) {
        savedStateHandle[TOPIC_ID_KEY] = topicId
    }
}