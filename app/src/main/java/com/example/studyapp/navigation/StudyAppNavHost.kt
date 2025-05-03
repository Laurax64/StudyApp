package com.example.studyapp.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studyapp.ui.screens.SubtopicScreen
import com.example.studyapp.ui.screens.SubtopicsScreen
import com.example.studyapp.ui.screens.TopicsScreen

@Composable
fun StudyAppNavHost(windowSize: WindowWidthSizeClass, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = TopicsRoute, modifier = modifier
    ) {
        topicsScreen(
            windowWidthSize = windowSize,
            navigateToTopic = { navController.navigate(route = SubtopicsRoute(topicId = it)) }
        )
        subtopicsScreen(navController = navController)
        subtopicScreen(navigateBack = navController::popBackStack)


    }
}

fun NavGraphBuilder.topicsScreen(
    windowWidthSize: WindowWidthSizeClass,
    navigateToTopic: (Int) -> Unit
) {
    composable<TopicsRoute> {
        TopicsScreen(
            topicsViewModel = hiltViewModel(),
            windowWidthSize = windowWidthSize,
            navigateToTopic = navigateToTopic
        )
    }
}

fun NavGraphBuilder.subtopicsScreen(
    navController: NavController,
) {
    composable<SubtopicsRoute> {
        SubtopicsScreen(
            subtopicsViewModel = hiltViewModel(),
            navigateToSubtopic = { subtopicId ->
                navController.navigate(route = SubtopicRoute(subtopicId = subtopicId))
            },
            navigateBack = navController::popBackStack
        )
    }
}

fun NavGraphBuilder.subtopicScreen(navigateBack: () -> Unit) {
    composable<SubtopicRoute> { entry ->
        SubtopicScreen(
            subtopicViewModel = hiltViewModel(),
            navigateBack = navigateBack,
        )
    }
}