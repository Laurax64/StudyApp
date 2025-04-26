package com.example.studyapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyapp.R
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.TopicsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsScreen(
    topicsViewModel: TopicsViewModel,
    navigateToTopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSearchBar by rememberSaveable { mutableStateOf(false) }
    val topics by topicsViewModel.topics.collectAsStateWithLifecycle(initialValue = listOf())
    if (showSearchBar) {
        TopicsSearchBar(
            modifier = modifier,
            navigateToTopic = navigateToTopic,
            closeSearchBar = { showSearchBar = false },
            topics = topics
        )
    }
    TopicsScaffold(
        topics = topics,
        createTopic = topicsViewModel::saveTopic,
        navigateToTopic = navigateToTopic,
        openSearchBar = { showSearchBar = true },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicsScaffold(
    topics: List<Topic>?,
    createTopic: (Topic) -> Unit,
    navigateToTopic: (Int) -> Unit,
    openSearchBar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_search_24),
                        contentDescription = stringResource(R.string.topics_search),
                        modifier = Modifier.clickable { openSearchBar() }
                    )
                },
                actions = {/*TODO Add account icon*/ },
                title = { Text(text = stringResource(R.string.app_name)) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        floatingActionButton = {
            CreateTopicFAB(saveTopic = createTopic)
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        if (topics == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp)
            ) {
                items(topics.size) { index ->
                    val topic = topics[index]
                    TopicListItem(
                        topic = topic,
                        modifier = Modifier
                            .clickable { navigateToTopic(topic.id) }
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicsSearchBar(
    modifier: Modifier = Modifier,
    navigateToTopic: (Int) -> Unit,
    topics: List<Topic>?,
    closeSearchBar: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var filteredTopics by rememberSaveable { mutableStateOf(topics) }
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    filteredTopics = topics?.filter { topic ->
                        topic.title.contains(query, ignoreCase = true)
                    }
                },
                expanded = true,
                onExpandedChange = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.close_search),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { closeSearchBar() }
                    )
                }
            )
        },
        expanded = true,
        onExpandedChange = {},
        modifier = modifier,
        content = {
            if (topics == null) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(topics.size) { index ->
                        val topic = topics[index]
                        TopicListItem(
                            topic = topic,
                            modifier = Modifier.clickable { navigateToTopic(topic.id) }
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun TopicListItem(
    topic: Topic,
    modifier: Modifier
) {
    ListItem(
        headlineContent = { Text(topic.title, overflow = TextOverflow.Ellipsis, maxLines = 1) },
        modifier = modifier,
        trailingContent = {
            Checkbox(
                checked = topic.checked,
                enabled = false,
                onCheckedChange = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(size = 24.dp)
            )
        }
    )
}

@Composable
private fun CreateTopicFAB(saveTopic: (Topic) -> Unit, modifier: Modifier = Modifier) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    ExtendedFloatingActionButton(
        onClick = { showDialog = true },
        modifier = modifier,
        icon =
            {
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

@PreviewDynamicColors
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
            openSearchBar = {},
            createTopic = {},
        )
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    StudyAppTheme {
        TopicsScaffold(
            topics = null,
            navigateToTopic = {},
            openSearchBar = {},
            createTopic = {},
        )
    }
}
