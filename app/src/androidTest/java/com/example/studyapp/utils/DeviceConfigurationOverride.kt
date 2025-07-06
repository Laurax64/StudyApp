package com.example.studyapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

enum class DeviceSize(val dpSize: DpSize) {
    COMPACT_WIDTH_EXPANDED_HEIGHT(
        dpSize = DpSize(
            width = (WIDTH_DP_MEDIUM_LOWER_BOUND - 10).dp,
            height = HEIGHT_DP_EXPANDED_LOWER_BOUND.dp
        )
    ),
    MEDIUM_WIDTH_EXPANDED_HEIGHT(
        dpSize = DpSize(
            width = WIDTH_DP_MEDIUM_LOWER_BOUND.dp,
            height = HEIGHT_DP_EXPANDED_LOWER_BOUND.dp
        )
    ),
    EXPANDED_WIDTH_COMPACT_HEIGHT(
        dpSize = DpSize(
            width = WIDTH_DP_EXPANDED_LOWER_BOUND.dp,
            height = (HEIGHT_DP_MEDIUM_LOWER_BOUND - 10).dp
        )
    )
}

@Composable
fun DeviceConfigurationOverride(
    deviceSize: DeviceSize,
    content: @Composable () -> Unit
) {
    DeviceConfigurationOverride(
        override = DeviceConfigurationOverride.ForcedSize(
            size = deviceSize.dpSize
        )
    )
    {
        content()
    }
}

