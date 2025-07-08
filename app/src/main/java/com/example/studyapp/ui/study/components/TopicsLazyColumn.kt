package com.example.studyapp.ui.study.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studyapp.data.study.TopicWithProgress

@Composable
internal fun TopicsLazyColumn(
    topicsWithProgress: List<TopicWithProgress>,
    navigateToTopic: (Int) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    selectedTopicId: Int? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    // Hide keyboard when scrolling
    if (state.isScrollInProgress) {
        keyboardController?.hide()
    }

    LazyColumn(modifier = modifier, state = state) {
        items(topicsWithProgress) { topicWithProgress ->
            var colors = ListItemDefaults.colors()
            if (selectedTopicId == topicWithProgress.topic.id) {
                colors =
                    ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            }
            TopicListItem(
                topicWithProgress = topicWithProgress,
                colors = colors,
                modifier = Modifier
                    .clickable { navigateToTopic(topicWithProgress.topic.id) }
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TopicListItem(
    topicWithProgress: TopicWithProgress,
    modifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(),
) {
    ListItem(
        headlineContent = {
            Text(
                text = topicWithProgress.topic.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        modifier = modifier,
        trailingContent = {
            Checkbox(
                checked = topicWithProgress.checked,
                enabled = false,
                onCheckedChange = null,
                modifier = Modifier.size(size = 24.dp)
            )
        },
        colors = colors
    )
}