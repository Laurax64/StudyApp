package com.example.studyapp.ui.screens.subtopic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.components.StudyAppAsyncImage

@Composable
internal fun SubtopicAnswerCard(
    isScreenWidthCompact: Boolean,
    subtopic: Subtopic,
    modifier: Modifier = Modifier
) {
    if (isScreenWidthCompact) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StudyAppAsyncImage(
                model = subtopic.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = subtopic.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    } else {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StudyAppAsyncImage(
                model = subtopic.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = subtopic.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

    }
}