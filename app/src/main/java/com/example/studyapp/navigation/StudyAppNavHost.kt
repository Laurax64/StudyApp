package com.example.studyapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studyapp.ui.screens.SubtopicScreen
import com.example.studyapp.ui.screens.SubtopicsScreen
import com.example.studyapp.ui.screens.TopicsScreen
import com.example.studyapp.ui.viewmodels.SubtopicViewModel
import com.example.studyapp.ui.viewmodels.SubtopicsViewModel
import com.example.studyapp.ui.viewmodels.TopicsViewModel

@Composable
fun StudyAppNavHost(modifier: Modifier = Modifier) {
    NavHost(
        navController = rememberNavController(),
        startDestination = TopicsRoute,
        modifier = modifier
    ) {
        composable<TopicsRoute> {
            TopicsScreen(topicsViewModel = hiltViewModel<TopicsViewModel>())
        }

        composable<SubtopicsRoute> {
            SubtopicsScreen(subtopicsViewModel = hiltViewModel<SubtopicsViewModel>())
        }

        composable<SubtopicRoute> {
            SubtopicScreen(subtopicViewModel = hiltViewModel<SubtopicViewModel>())
        }
    }
}