package com.example.studyapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studyapp.navigation.TopLevelDestination
import com.example.studyapp.navigation.TopLevelDestination.AI_ASSISTANT
import com.example.studyapp.navigation.TopLevelDestination.DATES
import com.example.studyapp.navigation.TopLevelDestination.STUDY
import com.example.studyapp.navigation.navigateToAIAssistant
import com.example.studyapp.navigation.navigateToDates
import com.example.studyapp.navigation.navigateToTopics
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberStudyAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): StudyAppState {
    return remember(navController, coroutineScope) {
        StudyAppState(
            navController = navController,
        )
    }
}

@Stable
class StudyAppState(val navController: NavHostController) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry =
                navController.currentBackStackEntryFlow.collectAsState(initial = null)

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        when (topLevelDestination) {
            STUDY -> navController.navigateToTopics()
            DATES -> navController.navigateToDates()
            AI_ASSISTANT -> navController.navigateToAIAssistant()
        }
    }
}
