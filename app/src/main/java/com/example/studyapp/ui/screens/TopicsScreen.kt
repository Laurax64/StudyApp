package com.example.studyapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyapp.R
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.TopicsUiState
import com.example.studyapp.ui.viewmodels.TopicsViewModel

@Composable
fun TopicsRoute(
    onTopicClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    shouldHighlightSelectedTopic: Boolean = false,
    viewModel: TopicsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TopicsScreen(
        uiState = uiState,
        onTopicClick = {
            viewModel.onTopicClick(it)
            onTopicClick(it)
        },
        shouldHighlightSelectedTopic = shouldHighlightSelectedTopic,
        modifier = modifier,
    )
}

@Composable
internal fun TopicsScreen(
    uiState: TopicsUiState,
    onTopicClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    shouldHighlightSelectedTopic: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (uiState) {
            TopicsUiState.Loading ->
                CircularProgressIndicator()

            is TopicsUiState.Topics ->
                TopicsTabContent(
                    topics = uiState.topics,
                    onTopicClick = onTopicClick,
                    selectedTopicId = uiState.selectedTopicId,
                    shouldHighlightSelectedTopic = shouldHighlightSelectedTopic,
                )

            is TopicsUiState.Empty -> TopicsEmptyScreen()
        }
    }
}

@Composable
private fun TopicsEmptyScreen() {
    Text(text = stringResource(id = R.string.no_topics_available))
}

@Composable
fun TopicsTabContent(
    topics: List<Topic>,
    onTopicClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    withBottomSpacer: Boolean = true,
    selectedTopicId: Int? = null,
    shouldHighlightSelectedTopic: Boolean = false,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        val scrollableState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            state = scrollableState,
        ) {
            topics.forEach { topic ->
                val topicId = topic.id
                item(key = topicId) {
                    val isSelected = shouldHighlightSelectedTopic && topicId == selectedTopicId

                    ListItem(
                        headlineContent = { Text(text = topic.title) },
                        trailingContent = {
                            Checkbox(
                                checked = topic.checked,
                                onCheckedChange = null,
                                enabled = false,
                            )
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = if (isSelected) {
                                MaterialTheme.colorScheme.surfaceVariant
                            } else {
                                Color.Transparent
                            },
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics(mergeDescendants = true) {
                                selected = isSelected
                            }
                            .clickable(enabled = true, onClick = { onTopicClick(topicId) }),
                    )
                }
            }

            if (withBottomSpacer) {
                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
fun TopicsScreenPopulated() {
    StudyAppTheme {
        TopicsScreen(
            uiState = TopicsUiState.Topics(
                selectedTopicId = null,
                topics = listOf(
                    Topic(1, "Topic 1", false),
                    Topic(2, "Topic 2", true),
                    Topic(3, "Topic 3", false)
                ),
            ),
            onTopicClick = {},
        )
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
fun InterestsScreenLoading() {
    StudyAppTheme {
        TopicsScreen(
            uiState = TopicsUiState.Loading,
            onTopicClick = {},
        )
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
fun TopicsScreenEmpty() {
    StudyAppTheme {
        TopicsScreen(
            uiState = TopicsUiState.Empty,
            onTopicClick = {},
        )
    }
}