package com.example.studyapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType.Companion.None
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType.Companion.ShortNavigationBarCompact
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType.Companion.ShortNavigationBarMedium
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType.Companion.WideNavigationRailCollapsed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.window.core.layout.WindowSizeClass
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
    val isTopLevelDestination = appState.topLevelDestinations.any { destination ->
        currentDestination.isRouteInHierarchy(destination.route)
    }
    val navigationSuiteType = calculateNavigationSuiteType(
        windowAdaptiveInfo = windowAdaptiveInfo,
        isTopLevelDestination = isTopLevelDestination
    )
    val windowSizeClass = windowAdaptiveInfo.windowSizeClass
    NavigationSuiteScaffold(
        navigationItemVerticalArrangement =
            if (windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)) {
                Arrangement.Center
            } else {
                // Top arrangement for screens with compact height
                Arrangement.Top
            },
        navigationItems = {
            appState.topLevelDestinations.forEach { destination ->
                val selected = currentDestination.isRouteInHierarchy(destination.route)
                NavigationSuiteItem(
                    selected = selected,
                    navigationSuiteType = navigationSuiteType,
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
        navigationSuiteType = navigationSuiteType,
        content = {
            StudyAppNavHost(appState = appState)
        }
    )
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any { it.hasRoute(route) } == true

/**
 * Calculates the navigation suite type based on the adaptive info and whether the current destination
 * is a top level destination.
 *
 *
 * @param windowAdaptiveInfo The adaptive info to calculate the navigation suite type from.
 * @param isTopLevelDestination Whether the current destination is a top level destination.
 * @return The calculated navigation suite type.
 */
fun calculateNavigationSuiteType(
    windowAdaptiveInfo: WindowAdaptiveInfo,
    isTopLevelDestination: Boolean
): NavigationSuiteType {
    return if (!isTopLevelDestination) {
        None
    } else {
        with(windowAdaptiveInfo) {
            val minWidthDp = windowSizeClass.minWidthDp
            when {
                minWidthDp < WIDTH_DP_MEDIUM_LOWER_BOUND -> ShortNavigationBarCompact
                minWidthDp < WIDTH_DP_EXPANDED_LOWER_BOUND -> ShortNavigationBarMedium
                else -> WideNavigationRailCollapsed
            }
        }
    }
}