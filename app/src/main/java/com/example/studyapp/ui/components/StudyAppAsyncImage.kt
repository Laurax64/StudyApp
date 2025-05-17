package com.example.studyapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage

@Composable
fun StudyAppAsyncImage(
    model: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    if (!model.isNullOrBlank()) {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}

