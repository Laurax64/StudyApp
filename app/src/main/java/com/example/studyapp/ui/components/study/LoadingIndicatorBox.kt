package com.example.studyapp.ui.components.study

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview

/**
 * A box that displays a loading indicator and fills the maximum size.
 *
 * @param modifier The modifier to be applied to the box.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingIndicatorBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("LoadingIndicatorBox"),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator()
    }
}

@Preview
@Composable
fun LoadingIndicatorBoxPreview() {
    LoadingIndicatorBox()
}
