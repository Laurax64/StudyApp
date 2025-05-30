package com.example.studyapp.ui.components.study

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studyapp.data.Topic

@Composable
internal fun TopicsLazyColumn(
    topics: List<Topic>,
    navigateToTopic: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedTopicId: Int? = null,
) {
    LazyColumn(modifier = modifier) {
        items(topics) { topic ->
            var colors = ListItemDefaults.colors()
            if (selectedTopicId == topic.id) {
                colors =
                    ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            }
            TopicListItem(
                topic = topic,
                colors = colors,
                modifier = Modifier
                    .clickable { navigateToTopic(topic.id) }
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TopicListItem(
    topic: Topic,
    modifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(),
) {
    ListItem(
        headlineContent = {
            Text(
                text = topic.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        modifier = modifier,
        trailingContent = {
            Checkbox(
                checked = topic.checked,
                enabled = false,
                onCheckedChange = null,
                modifier = Modifier.size(size = 24.dp)
            )
        },
        colors = colors
    )
}