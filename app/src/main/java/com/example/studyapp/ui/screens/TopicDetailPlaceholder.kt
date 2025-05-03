package com.example.studyapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.ui.theme.StudyAppTheme

@Composable
fun TopicDetailPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            20.dp,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_local_florist_24),
            contentDescription = null,
        )
        Text(
            text = stringResource(id = R.string.select_a_topic),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview(widthDp = 200, heightDp = 300)
@Composable
fun TopicDetailPlaceholderPreview() {
    StudyAppTheme {
        TopicDetailPlaceholder()
    }
}