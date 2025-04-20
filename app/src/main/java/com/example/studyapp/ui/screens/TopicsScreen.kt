package com.example.studyapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyapp.R
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.components.FullScreenDialog
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
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    if (showSettingsDialog) {
        SettingsFullScreenDialog(
            closeDialog = { showSettingsDialog = false },
            saveSettings = { showSettingsDialog = false },
            modifier = modifier
        )
    } else {
        val topics by topicsViewModel.topics.collectAsStateWithLifecycle(initialValue = listOf())
        if (showSearchBar) {
            TopicsSearchBar(
                modifier = modifier,
                updateChecked = topicsViewModel::updateChecked,
                navigateToTopic = navigateToTopic,
                closeSearchBar = { showSearchBar = false },
                topics = topics
            )
        }
        TopicsScaffold(
            topics = topics,
            createTopic = topicsViewModel::createTopic,
            navigateToTopic = navigateToTopic,
            updateChecked = topicsViewModel::updateChecked,
            openSearchBar = { showSearchBar = true },
            openSettings = { showSettingsDialog = true },
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicsScaffold(
    topics: List<Topic>?,
    createTopic: (String) -> Unit,
    updateChecked: (Topic, Boolean) -> Unit,
    navigateToTopic: (Int) -> Unit,
    openSearchBar: () -> Unit,
    openSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_search_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(R.string.topics_search),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { openSearchBar() }
                    )
                },
                actions = {
                    MoreActionsMenu(
                        openSettings = openSettings,
                    )
                },
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        floatingActionButton = {
            CreateTopicFAB(onCreate = createTopic)
        }
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
                        updateChecked = { checked -> updateChecked(topic, checked) },
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
    updateChecked: (Topic, Boolean) -> Unit,
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
                            updateChecked = { checked -> updateChecked(topic, checked) },
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
    updateChecked: (Boolean) -> Unit,
    modifier: Modifier
) {
    ListItem(
        headlineContent = { Text(topic.title) },
        modifier = modifier,
        trailingContent = {
            Checkbox(
                checked = topic.checked,
                onCheckedChange = updateChecked,
                modifier = Modifier
                    .padding(8.dp)
                    .size(size = 24.dp)
            )
        }
    )
}

@Composable
private fun MoreActionsMenu(
    openSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(modifier, horizontalAlignment = Alignment.End) {
        Icon(
            imageVector = Icons.Default.MoreVert, // TODO replace with M3 icon
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = stringResource(R.string.menu),
            modifier = Modifier.clickable { expanded = true }
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.share)) },
                onClick = { /* TODO Implement */ },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Share, // TODO replace with material 3 icon
                        contentDescription = null // TODO add content description
                    )
                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(stringResource(R.string.settings)) },
                onClick = openSettings,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Settings, // TODO replace with material 3 icon
                        contentDescription = stringResource(R.string.open_settings)
                    )
                }
            )
        }
    }
}

@Composable
private fun CreateTopicFAB(onCreate: (String) -> Unit, modifier: Modifier = Modifier) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    FloatingActionButton(onClick = { showDialog = true }, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.baseline_add_24),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            contentDescription = stringResource(R.string.create_topic),
            modifier = Modifier.size(24.dp)
        )
    }
    if (showDialog) {
        CreateTopicDialog(
            onDismiss = { showDialog = false },
            onCreate = onCreate
        )
    }
}

@Composable
private fun CreateTopicDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { },
    onCreate: (String) -> Unit = { }
) {
    var topicTitle by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.create_topic)) },
        text = {
            TextField(
                value = topicTitle,
                onValueChange = { topicTitle = it }, // Update the state with new value
                label = { Text(stringResource(R.string.title)) }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onCreate(topicTitle)
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.create))
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

@Composable
fun SettingsFullScreenDialog(
    closeDialog: () -> Unit,
    saveSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FullScreenDialog(
        titleRes = R.string.settings,
        onDismiss = closeDialog,
        onConfirm = saveSettings,
        modifier = modifier.padding(horizontal = 16.dp)
    ) { innerPadding ->
        // TODO: Add settings content here
    }
}


@PreviewDynamicColors
@PreviewLightDark
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
            openSettings = {},
            updateChecked = { _, _ -> }
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
            openSettings = {},
            updateChecked = { _, _ -> }
        )
    }
}
