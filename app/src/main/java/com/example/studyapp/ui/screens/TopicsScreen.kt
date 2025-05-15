package com.example.studyapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyapp.R
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.components.PlaceholderColumn
import com.example.studyapp.ui.components.StudyAppSearchBar
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.TopicsViewModel

@Composable
fun TopicsScreen(
    topicsViewModel: TopicsViewModel,
    navigateToTopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val topics by topicsViewModel.topics.collectAsStateWithLifecycle()
    TopicsScaffold(
        topics = topics,
        saveTopic = topicsViewModel::saveTopic,
        navigateToTopic = navigateToTopic,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun TopicsScaffold(
    topics: List<Topic>?,
    saveTopic: (Topic) -> Unit,
    navigateToTopic: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    var showSearchBar by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            if (!showSearchBar) {
                TopicsTopAppBar(
                    onSearch = { showSearchBar = true },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        },
        floatingActionButton = {
            CreateTopicFAB(saveTopic = saveTopic)
        },
    ) { innerPadding ->
        NavigableListDetailPaneScaffold(
            navigator = scaffoldNavigator,
            listPane = {
                AnimatedPane {
                    TopicsTabContent(
                        navigateToTopic = {
                            // Not scaffoldNavigator.navigateTo because the app needs to
                            // change more than just the detail pane
                            navigateToTopic(it)
                        },
                        closeSearchBar = { showSearchBar = false },
                        showSearchBar = showSearchBar,
                        topics = topics,
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    if (topics == null) {
                        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else {
                        val currentKey = scaffoldNavigator.currentDestination?.contentKey
                        if (currentKey != null && currentKey is Int) {
                            navigateToTopic(currentKey)
                        } else {
                            PlaceholderColumn(
                                textId =
                                    if (topics.isEmpty()) {
                                        R.string.no_subtopics_exist
                                    } else {
                                        R.string.select_a_topic
                                    },
                                iconId = R.drawable.outline_subtitles_24,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicsTopAppBar(
    modifier: Modifier = Modifier,
    onSearch: () -> Unit,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.baseline_search_24),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = stringResource(R.string.topics_search),
                modifier = Modifier.clickable { onSearch() }
            )
        },
        actions = {
            Icon(
                painter = painterResource(R.drawable.baseline_account_circle_24),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = stringResource(R.string.topics_search)
            )
        },
        title = { Text(text = stringResource(R.string.app_name)) },
        modifier = modifier
    )
}

@Composable
private fun TopicsTabContent(
    topics: List<Topic>?,
    modifier: Modifier = Modifier,
    navigateToTopic: (Int) -> Unit,
    closeSearchBar: () -> Unit,
    showSearchBar: Boolean
) {
    if (topics == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
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
                modifier = modifier.padding(horizontal = 8.dp).fillMaxSize()
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
    if (topics.isEmpty()) {
        PlaceholderColumn(
            textId = R.string.no_topics_exist,
            iconId = R.drawable.outline_topic_24,
            modifier = modifier,
        )
    } else {
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
        onItemClick = { topic -> navigateToTopic(topic.id) },
        closeSearchBar = closeSearchBar,
        itemLabel = { it.title },
        placeholderText = stringResource(R.string.search_in_topics)
    ) { topic ->
        TopicListItem(topic = topic, modifier = Modifier.fillMaxWidth())
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
                topic.title,
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

@Composable
private fun CreateTopicFAB(saveTopic: (Topic) -> Unit, modifier: Modifier = Modifier) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    ExtendedFloatingActionButton(
        onClick = { showDialog = true },
        modifier = modifier,
        icon = {
            Icon(
                painter = painterResource(R.drawable.baseline_add_24),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = stringResource(R.string.create_topic),
                modifier = Modifier.size(24.dp)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.create_topic),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
    )
    if (showDialog) {
        CreateTopicDialog(
            onDismiss = { showDialog = false },
            topic = null,
            onSave = saveTopic
        )
    }
}

@Composable
fun CreateTopicDialog(
    topic: Topic?,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSave: (Topic) -> Unit
) {
    var topicTitle by rememberSaveable { mutableStateOf(topic?.title ?: "") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.create_topic)) },
        text = {
            OutlinedTextField(
                value = topicTitle,
                onValueChange = { topicTitle = it },
                label = { Text(stringResource(R.string.title)) }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (topic != null) {
                        onSave(topic.copy(title = topicTitle))
                    } else {
                        onSave(Topic(title = topicTitle, checked = false))
                    }
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        modifier = modifier
    )
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun TopicsScreenPreview() {
    StudyAppTheme {
        TopicsScaffold(
            topics = listOf(
                Topic(1, "Topic 1", false),
                Topic(2, "Topic 2", true),
                Topic(3, "Topic 3", false)
            ),
            navigateToTopic = {},
            saveTopic = {},
        )
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun LoadingScreenPreview() {
    StudyAppTheme {
        TopicsScaffold(
            topics = null,
            navigateToTopic = {},
            saveTopic = {},
        )
    }
}