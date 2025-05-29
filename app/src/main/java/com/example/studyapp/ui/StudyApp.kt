package com.example.studyapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType.Companion.WideNavigationRailCollapsed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.example.studyapp.navigation.StudyAppNavHost
import kotlin.reflect.KClass

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun StudyApp(
    appState: StudyAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val currentDestination = appState.currentDestination
    val layoutType = calculateFromAdaptiveInfo(windowAdaptiveInfo)
    NavigationSuiteScaffold(
        primaryActionContent = {
            if (layoutType == WideNavigationRailCollapsed) {
                FloatingActionButton(
                    modifier = Modifier.padding(start = 20.dp),
                    onClick = {},
                    content = {}
                )
            } else {
                MediumFloatingActionButton(
                    onClick = {}) {

                }
            }
        }, navigationItems = {
            appState.topLevelDestinations.forEach { destination ->
                val selected = currentDestination.isRouteInHierarchy(destination.route)
                NavigationSuiteItem(
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
        }, modifier = modifier, navigationSuiteType = layoutType, content = {
            StudyAppNavHost(appState = appState)
        })
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any { it.hasRoute(route) } == true

/**
 * Calculates the navigation suite type based on the adaptive info.
 *
 * This is a modified version of the original function to use a navigation rail for
 * mobile phones in landscape orientation with an expanded width size class.
 *
 * @param adaptiveInfo The adaptive info to calculate the navigation suite type from.
 */
fun calculateFromAdaptiveInfo(adaptiveInfo: WindowAdaptiveInfo): NavigationSuiteType {
    return with(adaptiveInfo) {
        val minWidthDp = windowSizeClass.minWidthDp
        when {
            minWidthDp < WIDTH_DP_MEDIUM_LOWER_BOUND -> NavigationSuiteType.ShortNavigationBarCompact
            minWidthDp < WIDTH_DP_EXPANDED_LOWER_BOUND -> NavigationSuiteType.ShortNavigationBarMedium
            else -> WideNavigationRailCollapsed
        }
    }
}