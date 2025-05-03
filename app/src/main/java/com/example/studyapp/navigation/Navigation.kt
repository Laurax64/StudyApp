package com.example.studyapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable


fun NavController.navigateToStudy(navOptions: NavOptions? = null) {
    this.navigate(TopLevelDestination.STUDY.route, navOptions)
}

fun NavController.navigateToBookmarks(navOptions: NavOptions? = null) {
    this.navigate(BookmarksRoute, navOptions)
}

fun NavController.navigateToDates(navOptions: NavOptions? = null) {
    this.navigate(DatesRoute, navOptions)
}

fun NavController.navigateToAIAssistant(navOptions: NavOptions? = null) {
    this.navigate(AIAssistantRoute, navOptions)
}

fun NavGraphBuilder.aIAssistantSection() {
    composable<AIAssistantRoute> {/* TODO: Implement AI Assistant screen */ }
}


