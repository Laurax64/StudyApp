package com.example.studyapp.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.studyapp.navigation.StudyAppNavHost
import kotlin.reflect.KClass

@Composable
fun StudyApp(
    appState: StudyAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val currentDestination = appState.currentDestination
    val layoutType = calculateFromAdaptiveInfo(windowAdaptiveInfo)

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
        layoutType = layoutType,
        content = {
            StudyAppNavHost(appState = appState)
        }
    )
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } == true

/**
 * Calculates the navigation suite type based on the adaptive info.
 *
 * This is a modified version of the original function to use a navigation rail for
 * mobile phones in landscape orientation with a medium or expanded width size class.
 *
 * @param adaptiveInfo The adaptive info to calculate the navigation suite type from.
 */
private fun calculateFromAdaptiveInfo(adaptiveInfo: WindowAdaptiveInfo): NavigationSuiteType {
    return with(adaptiveInfo) {
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
            NavigationSuiteType.NavigationBar
        } else if (
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED ||
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM
        ) {
            NavigationSuiteType.NavigationRail
        } else {
            NavigationSuiteType.NavigationBar
        }
    }
}