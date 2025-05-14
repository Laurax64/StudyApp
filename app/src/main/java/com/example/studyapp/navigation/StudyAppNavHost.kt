package com.example.studyapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.studyapp.ui.StudyAppState
import com.example.studyapp.ui.screens.SubtopicScreen
import com.example.studyapp.ui.screens.SubtopicsScreen
import com.example.studyapp.ui.screens.TopicsScreen

@Composable
fun StudyAppNavHost(
    appState: StudyAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(
        navController = navController, startDestination = StudyRoute, modifier = modifier
    ) {
        navigation<StudyRoute>(startDestination = TopicsRoute) {
            topicsScreen(navigateToTopic = { navController.navigate(route = SubtopicsRoute(topicId = it)) })
            subtopicsScreen(
                navigateToSubtopic = { navController.navigate(route = SubtopicRoute(subtopicId = it)) },
                navigateToTopic = { navController.navigate(route = SubtopicsRoute(topicId = it)) },
                navigateBack = navController::popBackStack
            )
            subtopicScreen(navigateBack = navController::popBackStack)
        }
        datesScreen()
        aIAssistantScreen()
        bookmarksScreen()
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

fun NavGraphBuilder.subtopicScreen(navigateBack: () -> Unit) {
    composable<SubtopicRoute> {
        SubtopicScreen(
            subtopicViewModel = hiltViewModel(),
            navigateBack = navigateBack,
        )
    }
}

fun NavGraphBuilder.studyScreen(){

}

fun NavGraphBuilder.datesScreen() {
    composable<DatesRoute> {/* TODO: Implement Dates screen */ }
}

fun NavGraphBuilder.aIAssistantScreen() {
    composable<AIAssistantRoute> {/* TODO: Implement AI Assistant screen */ }
}

fun NavGraphBuilder.bookmarksScreen() {
    composable<BookmarksRoute> {/* TODO: Implement Bookmarks screen */ }
}