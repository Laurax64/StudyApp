package com.example.studyapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
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
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.components.SubtopicFullScreenDialog
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.SubtopicsViewModel

@Composable
fun SubtopicsScreen(
    subtopicsViewModel: SubtopicsViewModel,
    navigateToSubtopic: (Int) -> Unit,
    navigateToTopic: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topic by subtopicsViewModel.topic.collectAsStateWithLifecycle()
    val subtopics by subtopicsViewModel.subtopics.collectAsStateWithLifecycle()
    val topics by subtopicsViewModel.topics.collectAsStateWithLifecycle()
    SubtopicsScaffold(
        subtopics = subtopics,
        topics = topics,
        saveSubtopic = { title, description, imageUri ->
            subtopicsViewModel.createSubtopic(
                title = title, description = description, imageUri = imageUri
            )
        },
        deleteTopic = subtopicsViewModel::deleteTopic,
        updateTopic = subtopicsViewModel::updateTopic,
        navigateToSubtopic = navigateToSubtopic,
        navigateToTopic = navigateToTopic,
        navigateBack = navigateBack,
        topic = topic ?: return,
        modifier = modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun SubtopicsScaffold(
    subtopics: List<Subtopic>?,
    topics: List<Topic>?,
    topic: Topic?,
    saveSubtopic: (String, String, String?) -> Unit,
    deleteTopic: () -> Unit,
    updateTopic: (Topic) -> Unit,
    navigateToSubtopic: (Int) -> Unit,
    navigateToTopic: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (topic == null || subtopics == null || topics == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        if (showDialog) {
            SubtopicFullScreenDialog(
                titleRes = R.string.create_subtopic,
                onDismiss = { showDialog = false },
                saveSubtopic = { title, description, imageUri ->
                    saveSubtopic(title, description, imageUri)
                    showDialog = false
                },
                modifier = modifier
            )
        } else {
            Scaffold(
                modifier = modifier,
                topBar = {
                    SubtopicsTopAppBar(
                        topic = topic,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        deleteTopic = deleteTopic,
                        updateTopic = updateTopic,
                        navigateBack = navigateBack
                    )
                },
                floatingActionButton = { CreateSubtopicFAB(onCreate = { showDialog = true }) },
            ) { innerPadding ->
                NavigableListDetailPaneScaffold(
                    navigator = scaffoldNavigator,
                    listPane = {
                        AnimatedPane {
                            if (scaffoldNavigator.scaffoldState.currentState.secondary == PaneAdaptedValue.Expanded) {
                                ScrollableTopicsList(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    topics = topics,
                                    navigateToTopic = navigateToTopic
                                )
                            } else {
                                if (scaffoldNavigator.scaffoldState.currentState.secondary == PaneAdaptedValue.Hidden) {
                                    ScrollableSubtopicsList(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        subtopics = subtopics,
                                        navigateToSubtopic = navigateToSubtopic
                                    )
                                }
                            }
                        }
                    },
                    detailPane = {
                        AnimatedPane {
                            ScrollableSubtopicsList(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                subtopics = subtopics,
                                navigateToSubtopic = navigateToSubtopic
                            )
                        }
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubtopicsTopAppBar(
    modifier: Modifier = Modifier,
    topic: Topic,
    deleteTopic: () -> Unit,
    updateTopic: (Topic) -> Unit,
    navigateBack: () -> Unit
) {
    LargeTopAppBar(
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = stringResource(R.string.go_back_to_topics),
                modifier = Modifier.clickable { navigateBack() })
        },
        actions = {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Icon(
                    painter = painterResource(R.drawable.baseline_search_24),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable {/*TODO*/ },
                    contentDescription = stringResource(R.string.subtopics_search),
                )
                MoreActionsMenu(
                    deleteTopic = deleteTopic,
                    updateTopic = updateTopic,
                    topic = topic,
                )
            }
        },
        title = { Text(text = topic.title, overflow = Ellipsis) },
        modifier = modifier
    )
}

@Composable
private fun MoreActionsMenu(
    deleteTopic: () -> Unit,
    updateTopic: (Topic) -> Unit,
    topic: Topic,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    if (showEditDialog) {
        EditTopicDialog(
            onDismiss = { showEditDialog = false },
            topic = topic,
            onSave = {
                updateTopic(it)
                showEditDialog = false
            },
        )
    } else if (showDeleteDialog) {
        DeleteTopicDialog(
            topicTitle = topic.title,
            deleteTopic = {
                deleteTopic()
                showDeleteDialog = false
            },
            closeDialog = { showDeleteDialog = false },
        )
    }
    Column(modifier, horizontalAlignment = Alignment.End) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = stringResource(R.string.menu),
            modifier = Modifier.clickable { expanded = true })
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.share)) },
                onClick = { /* TODO Implement */ },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Share, contentDescription = null
                    )
                })
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit)) },
                onClick = { showEditDialog = true },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_create_24),
                        contentDescription = stringResource(R.string.edit_topic)
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                onClick = { showDeleteDialog = true },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_delete_outline_24),
                        contentDescription = stringResource(R.string.delete_topic)
                    )
                }
            )
        }
    }
}


@Composable
fun ScrollableSubtopicsList(
    modifier: Modifier,
    subtopics: List<Subtopic>,
    navigateToSubtopic: (Int) -> Unit
) {
    var showOnlyNotChecked by rememberSaveable { mutableStateOf(false) }
    var showOnlyBookmarked by rememberSaveable { mutableStateOf(false) }
    val filteredSubtopics =
        subtopics.filter {
            !(it.checked && showOnlyNotChecked) && (it.bookmarked || !showOnlyBookmarked)
        }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                onClick = { showOnlyNotChecked = !showOnlyNotChecked },
                label = { Text(text = stringResource(R.string.unchecked)) },
                selected = showOnlyNotChecked,
            )
            FilterChip(
                onClick = { showOnlyBookmarked = !showOnlyBookmarked },
                label = { Text(text = stringResource(R.string.bookmarked)) },
                selected = showOnlyBookmarked,
            )
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(filteredSubtopics) { subtopic ->
                SubtopicListItem(
                    subtopic = subtopic,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigateToSubtopic(subtopic.id) })
            }
        }
    }
}

@Composable
private fun SubtopicListItem(subtopic: Subtopic, modifier: Modifier) {
    ListItem(
        headlineContent = { Text(subtopic.title) },
        modifier = modifier,
        leadingContent = {
            Icon(
                painter = painterResource(
                    id = if (subtopic.bookmarked) {
                        R.drawable.baseline_bookmark_24
                    } else {
                        R.drawable.baseline_bookmark_border_24
                    }
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(size = 24.dp),
                contentDescription = stringResource(
                    id = if (subtopic.bookmarked) {
                        R.string.bookmarked
                    } else {
                        R.string.bookmarked
                    }
                )
            )
        },
        trailingContent = {
            Checkbox(
                checked = subtopic.checked,
                enabled = false,
                onCheckedChange = null,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(size = 24.dp)
            )
        },
    )
}


@Composable
fun EditTopicDialog(
    topic: Topic?, modifier: Modifier = Modifier, onDismiss: () -> Unit, onSave: (Topic) -> Unit
) {
    var topicTitle by rememberSaveable { mutableStateOf(topic?.title ?: "") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.edit_topic)) },
        text = {
            OutlinedTextField(
                value = topicTitle,
                onValueChange = { topicTitle = it },
                label = { Text(stringResource(R.string.title)) })
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
                }) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        },
        modifier = modifier
    )
}

@Composable
fun DeleteTopicDialog(
    modifier: Modifier = Modifier,
    topicTitle: String,
    deleteTopic: () -> Unit,
    closeDialog: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { deleteTopic() },
        title = { Text(stringResource(R.string.delete_topic_dialog_title)) },
        text = {
            Text(
                stringResource(R.string.delete_topic_dialog_description, topicTitle)
            )
        },
        confirmButton = {
            TextButton(onClick = deleteTopic) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = closeDialog) {
                Text(stringResource(R.string.cancel))
            }
        },
        modifier = modifier
    )
}


@Composable
private fun CreateSubtopicFAB(onCreate: () -> Unit, modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        onClick = onCreate,
        modifier = modifier,
        icon = {
            Icon(
                painter = painterResource(R.drawable.baseline_add_24),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = stringResource(R.string.create_subtopic),
                modifier = Modifier.size(24.dp)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.create_subtopic),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
    )
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun SubtopicsScreenPreview() {
    StudyAppTheme {
        SubtopicsScaffold(
            subtopics = listOf(
                Subtopic(
                    id = 1,
                    topicId = 0,
                    title = "Subtopic 1",
                    description = "Description 1",
                    checked = false,
                    bookmarked = false,
                    imageUri = null
                ), Subtopic(
                    id = 2,
                    topicId = 0,
                    title = "Subtopic 2",
                    description = "Description 2",
                    checked = true,
                    bookmarked = true,
                    imageUri = null
                ), Subtopic(
                    id = 3,
                    topicId = 0,
                    title = "Subtopic 3",
                    description = "Description 3",
                    checked = false,
                    bookmarked = false,
                    imageUri = null
                )
            ),
            topics = listOf(Topic(id = 1, title = "Topic 1", checked = false)),
            topic = Topic(
                id = 1, title = "Android Taint Analysis", checked = false
            ),
            navigateToSubtopic = {},
            navigateToTopic = {},
            navigateBack = {},
            saveSubtopic = { _, _, _ -> },
            deleteTopic = {},
            updateTopic = {})
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun LoadingScreenPreview() {
    StudyAppTheme {
        SubtopicsScaffold(
            subtopics = null,
            topics = null,
            topic = null,
            navigateToSubtopic = {},
            navigateToTopic = {},
            navigateBack = {},
            saveSubtopic = { _, _, _ -> },
            deleteTopic = {},
            updateTopic = {},
        )
    }
}
