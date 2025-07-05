package com.example.studyapp.study

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.ui.topics.TopicsScreen
import com.example.studyapp.ui.topics.TopicsUiState

@Preview(apiLevel = 35)
@PreviewTest
@Composable
private fun TopicsScreenPreviewCompact() {
    TopicsScreen(
        uiState = TopicsUiState.Success(
            topicsWithProgress = listOf(
                TopicWithProgress(
                    topic = Topic(1, "Dogs"),
                    checked = false
                ),
                TopicWithProgress(
                    topic = Topic(2, "Cats"),
                    checked = false
                ),
                TopicWithProgress(
                    topic = Topic(3, "Horses"),
                    checked = false
                ),
                TopicWithProgress(
                    topic = Topic(4, "Rabbits"),
                    checked = false
                ),
                TopicWithProgress(
                    topic = Topic(5, "Fish"),
                    checked = false
                ),
                TopicWithProgress(
                    topic = Topic(6, "Birds"),
                    checked = false
                ),
                TopicWithProgress(
                    topic = Topic(7, "Hamsters"),
                    checked = false
                ),
                TopicWithProgress(
                    topic = Topic(8, "Guinea pigs"),
                    checked = false
                ),
                TopicWithProgress(
                    topic = Topic(9, "Turtles"),
                    checked = false
                ),
                TopicWithProgress(
                    topic = Topic(10, "Elephants"),
                    checked = false
                )
            )
        ),
        addTopic = {},
        navigateToSubtopics = {},
    )
}