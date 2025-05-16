package com.example.studyapp.ui.screens.topics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.TopicsViewModel

@Composable
fun TopicsScreen(
    topicsViewModel: TopicsViewModel,
    navigateToTopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val topics by topicsViewModel.topics.collectAsStateWithLifecycle()
    TopicsScreen(
        topics = topics,
        saveTopic = topicsViewModel::saveTopic,
        navigateToSubtopics = navigateToTopic,
        modifier = modifier
    )
}

@Composable
private fun TopicsScreen(
    topics: List<Topic>?,
    saveTopic: (Topic) -> Unit,
    navigateToSubtopics: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (topics == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        TopicsScaffold(
            topics = topics,
            saveTopic = saveTopic,
            navigateToSubtopics = navigateToSubtopics,
            modifier = modifier
        )
    }
}


@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun TopicsScreenPreview() {
    StudyAppTheme {
        TopicsScreen(
            topics = listOf(
                Topic(1, "Topic 1", false), Topic(2, "Topic 2", true), Topic(3, "Topic 3", false)
            ),
            navigateToSubtopics = {},
            saveTopic = {},
        )
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun LoadingScreenPreview() {
    StudyAppTheme {
        TopicsScreen(
            topics = null,
            navigateToSubtopics = {},
            saveTopic = {},
        )
    }
}