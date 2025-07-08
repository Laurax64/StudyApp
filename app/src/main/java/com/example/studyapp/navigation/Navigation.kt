package com.example.studyapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions


fun NavController.navigateToTopics(navOptions: NavOptions? = null) {
    this.navigate(TopicsRoute, navOptions)
}

fun NavController.navigateToDates(navOptions: NavOptions? = null) {
    this.navigate(DatesRoute, navOptions)
}

fun NavController.navigateToAIAssistant(navOptions: NavOptions? = null) {
    this.navigate(AIAssistantRoute, navOptions)
}


