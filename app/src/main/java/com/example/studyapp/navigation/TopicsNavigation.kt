package com.example.studyapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions


fun NavController.navigateToTopics(
    initialTopicId: Int? = null,
    navOptions: NavOptions? = null,
) {
    navigate(route = TopicsRoute(initialTopicId), navOptions)
}