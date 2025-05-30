package com.example.studyapp.ui.components.study

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.example.studyapp.R
import com.example.studyapp.ui.theme.StudyAppTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AdaptiveFAB(
    iconId: Int,
    contentDescriptionId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    if (windowSizeClass.isWidthAtLeastBreakpoint(widthDpBreakpoint = WIDTH_DP_EXPANDED_LOWER_BOUND)) {
        LargeFloatingActionButton(
            onClick = onClick,
            modifier = modifier,
        )
        {
            Icon(
                painter = painterResource(iconId),
                contentDescription = stringResource(contentDescriptionId),
                modifier = Modifier.size(36.dp)
            )
        }
    } else if (windowSizeClass.isWidthAtLeastBreakpoint(widthDpBreakpoint = WIDTH_DP_MEDIUM_LOWER_BOUND)) {
        MediumFloatingActionButton(
            onClick = onClick,
            modifier = modifier,
        )
        {
            Icon(
                painter = painterResource(iconId),
                contentDescription = stringResource(contentDescriptionId),
                modifier = Modifier.size(28.dp)
            )
        }
    } else {
        FloatingActionButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = stringResource(contentDescriptionId),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@Composable
fun FABPreview() {
    StudyAppTheme {
        AdaptiveFAB(
            iconId = R.drawable.baseline_add_24,
            contentDescriptionId = R.string.create_topic,
            onClick = {},
        )
    }
}