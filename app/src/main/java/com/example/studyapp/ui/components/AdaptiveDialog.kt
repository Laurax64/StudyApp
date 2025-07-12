package com.example.studyapp.ui.components

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.example.studyapp.R

@Composable
fun AdaptiveDialog(
    titleResId: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmButtonTextResId: Int = R.string.save,
    dismissIconResId: Int = R.drawable.baseline_close_24,
    dismissButtonTextResId: Int = R.string.cancel,
    content: @Composable (Modifier) -> Unit,
) {
    val isScreenWidthCompact =
        !currentWindowAdaptiveInfo().windowSizeClass.isWidthAtLeastBreakpoint(
            WIDTH_DP_MEDIUM_LOWER_BOUND
        )
    if (isScreenWidthCompact) {
        FullScreenDialog(
            titleResId = titleResId,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
            modifier = modifier,
            confirmButtonTextResId = confirmButtonTextResId,
            dismissIconResId = dismissIconResId,
            content = content,
        )
    } else {
        AlertDialog(
            titleResId = titleResId,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
            modifier = modifier,
            confirmButtonTextResId = confirmButtonTextResId,
            dismissButtonTextResId = dismissButtonTextResId,
            content = content,
        )
    }
}