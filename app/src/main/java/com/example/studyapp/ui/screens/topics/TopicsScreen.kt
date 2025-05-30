package com.example.studyapp.ui.screens.topics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopicsScreen(
    topics: List<Topic>?,
    saveTopic: (Topic) -> Unit,
    navigateToSubtopics: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (topics == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingIndicator()
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


@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun TopicsScreenPreview() {
    StudyAppTheme {
        TopicsScreen(
            topics = listOf(
                Topic(1, "Dogs", false),
                Topic(2, "Cats", false),
                Topic(3, "Horses", false),
                Topic(4, "Rabbits", false),
                Topic(5, "Fish", false),
                Topic(6, "Birds", false),
                Topic(7, "Hamsters", false),
                Topic(8, "Guinea pigs", false),
                Topic(9, "Turtles", false),
                Topic(10, "Elephants", false)
            ),
            saveTopic = {},
            navigateToSubtopics = {},
        )
    }
}

@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun TopicsScreenLoadingPreview() {
    StudyAppTheme {
        TopicsScreen(
            topics = null,
            navigateToSubtopics = {},
            saveTopic = {},
        )
    }
}