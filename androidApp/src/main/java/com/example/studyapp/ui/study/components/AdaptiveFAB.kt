package com.example.studyapp.ui.study.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.example.studyapp.R
import com.example.studyapp.ui.theme.StudyAppTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AdaptiveFAB(
    iconId: Int,
    contentDescriptionId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
) {
    if (windowSizeClass.isWidthAtLeastBreakpoint(widthDpBreakpoint = WIDTH_DP_EXPANDED_LOWER_BOUND)) {
        LargeFloatingActionButton(
            onClick = onClick,
            modifier = modifier.testTag("LargeFloatingActionButton"),
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = stringResource(contentDescriptionId),
                modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize)
            )
        }
    } else if (windowSizeClass.isWidthAtLeastBreakpoint(widthDpBreakpoint = WIDTH_DP_MEDIUM_LOWER_BOUND)) {
        MediumFloatingActionButton(
            onClick = onClick,
            modifier = modifier.testTag("MediumFloatingActionButton")
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = stringResource(contentDescriptionId),
                modifier = Modifier.size(FloatingActionButtonDefaults.MediumIconSize)
            )
        }
    } else {
        FloatingActionButton(
            onClick = onClick,
            modifier = modifier.testTag("FloatingActionButton")
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = stringResource(contentDescriptionId),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
fun AdaptiveFABPreview() {
    StudyAppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AdaptiveFAB(
                iconId = R.drawable.baseline_add_24,
                contentDescriptionId = R.string.create_topic,
                onClick = {},
                windowSizeClass = WindowSizeClass(
                    minWidthDp = WIDTH_DP_MEDIUM_LOWER_BOUND - 1,
                    minHeightDp = 200
                )
            )
            AdaptiveFAB(
                iconId = R.drawable.baseline_add_24,
                contentDescriptionId = R.string.create_topic,
                onClick = {},
                windowSizeClass = WindowSizeClass(
                    minWidthDp = WIDTH_DP_MEDIUM_LOWER_BOUND,
                    minHeightDp = 200
                )
            )
            AdaptiveFAB(
                iconId = R.drawable.baseline_add_24,
                contentDescriptionId = R.string.create_topic,
                onClick = {},
                windowSizeClass = WindowSizeClass(
                    minWidthDp = WIDTH_DP_EXPANDED_LOWER_BOUND,
                    minHeightDp = 200
                )
            )
        }
    }
}