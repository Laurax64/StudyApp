package com.example.studyapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.studyapp.ui.StudyAppState
import com.example.studyapp.ui.subtopic.SubtopicScreen
import com.example.studyapp.ui.subtopics.SubtopicsScreen
import com.example.studyapp.ui.topics.TopicsScreen

@Composable
fun StudyAppNavHost(
    appState: StudyAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(navController = navController, startDestination = TopicsRoute, modifier = modifier) {
        topicsScreen(navigateToTopic = { navController.navigate(route = SubtopicsRoute(topicId = it)) })
        subtopicsScreen(
            navigateToSubtopic = { navController.navigate(route = SubtopicRoute(subtopicId = it)) },
            navigateToTopic = { navController.navigate(route = SubtopicsRoute(topicId = it)) },
            navigateBack = { navController.popBackStack() },
        )
        subtopicScreen(
            navigateBackToSubtopics = { topicId ->
                navController.popBackStack(
                    route = SubtopicsRoute(topicId = topicId),
                    inclusive = false
                )
            },
            navigateBack = navController::popBackStack,
            navigateToSubtopic = {
                navController.navigate(route = SubtopicRoute(subtopicId = it))
            }
        )
        datesScreen()
        aIAssistantScreen()
    }
}

fun NavGraphBuilder.topicsScreen(navigateToTopic: (Int) -> Unit) {
    composable<TopicsRoute> {
        TopicsScreen(
            topicsViewModel = hiltViewModel(),
            navigateToTopic = navigateToTopic
        )
    }
}

fun NavGraphBuilder.subtopicsScreen(
    navigateToSubtopic: (Int) -> Unit,
    navigateToTopic: (Int) -> Unit,
    navigateBack: () -> Unit
) {
    composable<SubtopicsRoute> {
        SubtopicsScreen(
            subtopicsViewModel = hiltViewModel(),
            navigateToSubtopic = navigateToSubtopic,
            navigateToTopic = navigateToTopic,
            navigateBack = navigateBack
        )
    }
}

fun NavGraphBuilder.subtopicScreen(
    navigateBackToSubtopics: (Int) -> Unit,
    navigateBack: () -> Unit,
    navigateToSubtopic: (Int) -> Unit
) {
    composable<SubtopicRoute> {
        SubtopicScreen(
            viewModel = hiltViewModel(),
            navigateToSubtopic = navigateToSubtopic,
            navigateBackToSubtopics = navigateBackToSubtopics,
            navigateBack = navigateBack
        )
    }
}

fun NavGraphBuilder.datesScreen() {
    composable<DatesRoute> {/* TODO: Implement Dates screen */ }
}

fun NavGraphBuilder.aIAssistantScreen() {
    composable<AIAssistantRoute> {/* TODO: Implement AI Assistant screen */ }
}