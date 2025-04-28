package com.example.studyapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.studyapp.ui.screens.SubtopicScreen
import com.example.studyapp.ui.screens.SubtopicsScreen
import com.example.studyapp.ui.screens.TopicsScreen
import com.example.studyapp.ui.viewmodels.SubtopicViewModel
import com.example.studyapp.ui.viewmodels.SubtopicsViewModel
import com.example.studyapp.ui.viewmodels.TopicsViewModel

@Composable
fun StudyAppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = TopicsRoute, modifier = modifier
    ) {
        composable<TopicsRoute> {
            TopicsScreen(
                topicsViewModel = hiltViewModel<TopicsViewModel>(),
                navigateToTopic = { navController.navigate(route = SubtopicsRoute(topicId = it)) }
            )
        }
        composable<SubtopicsRoute> {
            SubtopicsScreen(
                subtopicsViewModel = hiltViewModel<SubtopicsViewModel>(),
                navigateToSubtopic = { subtopicId ->
                    navController.navigate(route = SubtopicRoute(subtopicId = subtopicId))
                },
                navigateBack = navController::popBackStack
            )
        }

        composable<SubtopicRoute> { entry ->
            val subtopicId = entry.toRoute<SubtopicRoute>().subtopicId
            SubtopicScreen(
                subtopicViewModel = hiltViewModel<SubtopicViewModel, SubtopicViewModel.Factory>(
                    key = subtopicId.toString()
                ) { factory ->
                    factory.create(subtopicId)
                },
                navigateBack = navController::popBackStack
            )

        }
    }
}
