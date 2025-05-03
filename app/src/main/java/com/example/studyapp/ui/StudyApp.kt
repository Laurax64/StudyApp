package com.example.studyapp.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.studyapp.navigation.StudyAppNavHost
import com.example.studyapp.ui.utils.StudyAppContentType
import com.example.studyapp.ui.utils.StudyAppNavigationType
import kotlin.reflect.KClass

@Composable
fun StudyApp(
    appState: StudyAppState,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val currentDestination = appState.currentDestination
    val navigationType: StudyAppNavigationType
    val contentType: StudyAppContentType
    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = StudyAppNavigationType.BOTTOM_NAVIGATION
            contentType = StudyAppContentType.LIST_ONLY
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = StudyAppNavigationType.NAVIGATION_RAIL
            contentType = StudyAppContentType.LIST_ONLY
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = StudyAppNavigationType.NAVIGATION_RAIL
            contentType = StudyAppContentType.LIST_AND_DETAIL
        }

        else -> {
            navigationType = StudyAppNavigationType.BOTTOM_NAVIGATION
            contentType = StudyAppContentType.LIST_ONLY
        }
    }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            appState.topLevelDestinations.forEach { destination ->
                val selected = currentDestination.isRouteInHierarchy(destination.route)
                item(
                    selected = selected,
                    onClick = { appState.navigateToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            painter = painterResource(id = destination.iconRes),
                            contentDescription = stringResource(id = destination.contentDescriptionRes),
                        )
                    },
                    label = { Text(stringResource(destination.labelRes)) },
                )
            }
        },
        modifier = modifier,
        content = {
            StudyAppNavHost(windowSize = windowSize)
        }
    )
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } == true