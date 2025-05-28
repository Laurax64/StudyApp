package com.example.studyapp.ui.screens.topics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.components.PlaceholderColumn
import com.example.studyapp.ui.components.StudyAppSearchBar

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopicsPaneContent(
    topics: List<Topic>?,
    modifier: Modifier = Modifier,
    navigateToTopic: (Int) -> Unit,
    closeSearchBar: () -> Unit,
    showSearchBar: Boolean
) {
    if (topics == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingIndicator()
        }
    } else if (showSearchBar) {
        TopicsSearchBar(
            modifier = modifier.fillMaxWidth(),
            navigateToTopic = navigateToTopic,
            closeSearchBar = closeSearchBar,
            topics = topics
        )
    } else {
        if (topics.isEmpty()) {
            PlaceholderColumn(
                textId = R.string.no_topics_exist,
                iconId = R.drawable.outline_topic_24,
                modifier = modifier.fillMaxSize()
            )
        } else {
            ScrollableTopicsList(
                topics = topics,
                navigateToTopic = navigateToTopic,
                modifier = modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun ScrollableTopicsList(
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
private fun TopicsSearchBar(
    modifier: Modifier = Modifier,
    navigateToTopic: (Int) -> Unit,
    topics: List<Topic>,
    closeSearchBar: () -> Unit
) {
    StudyAppSearchBar(
        modifier = modifier,
        items = topics,
        closeSearchBar = closeSearchBar,
        itemLabel = { it.title },
        placeholderText = stringResource(R.string.search_in_topics)
    ) {
        ScrollableTopicsList(
            topics = topics,
            navigateToTopic = navigateToTopic,
            modifier = modifier
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        )
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
