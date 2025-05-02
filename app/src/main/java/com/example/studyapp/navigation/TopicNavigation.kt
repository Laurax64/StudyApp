package com.example.studyapp.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.studyapp.ui.screens.TopicScreen
import com.example.studyapp.ui.viewmodels.TopicViewModel


fun NavController.navigateToTopic(topicId: Int, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = TopicRoute(id = topicId)) {
        navOptions()
    }
}

fun NavGraphBuilder.topicScreen(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onTopicClick: (Int) -> Unit,
) {
    composable<TopicRoute> { entry ->
        val id = entry.toRoute<TopicRoute>().id
        TopicScreen(
            showBackButton = showBackButton,
            onBackClick = onBackClick,
            onTopicClick = onTopicClick,
            viewModel = hiltViewModel<TopicViewModel, TopicViewModel.Factory>(
                key = id.toString(),
            ) { factory ->
                factory.create(id)
            },
        )
    }
}