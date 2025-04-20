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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.components.SubtopicFullScreenDialog
import com.example.studyapp.ui.components.TopicDialog
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.SubtopicsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtopicsScreen(
    subtopicsViewModel: SubtopicsViewModel,
    navigateToSubtopic: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topic by subtopicsViewModel.topic.collectAsStateWithLifecycle()
    val subtopics by subtopicsViewModel.subtopics.collectAsStateWithLifecycle()
    SubtopicsScaffold(
        subtopics = subtopics ?: emptyList(),
        createSubtopic = { title, description, imageUri ->
            subtopicsViewModel.createSubtopic(
                title = title, description = description, imageUri = imageUri
            )
        },
        deleteTopic = subtopicsViewModel::deleteTopic,
        updateTopic = subtopicsViewModel::updateTopic,
        navigateToSubtopic = navigateToSubtopic,
        navigateBack = navigateBack,
        topic = topic ?: return,
        modifier = modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubtopicsScaffold(
    subtopics: List<Subtopic>?,
    topic: Topic?,
    createSubtopic: (String, String, String?) -> Unit,
    deleteTopic: () -> Unit,
    updateTopic: (Topic) -> Unit,
    navigateToSubtopic: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    if (topic == null || subtopics == null) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
    } else {
        if (showDialog) {
            SubtopicFullScreenDialog(
                titleRes = R.string.create_subtopic,
                onDismiss = { showDialog = false },
                saveSubtopic = { title, description, imageUri ->
                    createSubtopic(title, description, imageUri)
                    showDialog = false
                },
                modifier = modifier
            )
        } else {
            Scaffold(modifier = modifier, topBar = {
                TopAppBar(
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(R.string.go_back_to_topics),
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { navigateBack() })
                    }, actions = {
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_search_24),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .clickable {/*TODO*/ }
                                    .size(24.dp),
                                contentDescription = stringResource(R.string.subtopics_search),
                            )
                            MoreActionsMenu(
                                deleteTopic = deleteTopic,
                                updateTopic = updateTopic,
                                topic = topic,
                            )
                        }
                    }, title = {
                        Text(
                            text = topic.title, modifier = Modifier.padding(start = 16.dp)
                        )
                    }, modifier = Modifier.padding(horizontal = 16.dp)
                )
            }, floatingActionButton = {
                CreateSubtopicFAB(onCreate = { showDialog = true })
            }) { innerPadding ->
                LazyColumn(
                    Modifier
                        .padding(paddingValues = innerPadding)
                        .padding(horizontal = 16.dp)
                ) {
                    items(subtopics.size) { index ->
                        val subtopic = subtopics[index]
                        SubtopicListItem(
                            subtopic = subtopic,
                            modifier = Modifier.clickable { navigateToSubtopic(subtopic.id) })
                    }

                }
            }
        }
    }
}

@Composable
private fun SubtopicListItem(subtopic: Subtopic, modifier: Modifier) {
    ListItem(headlineContent = { Text(subtopic.title) }, modifier = modifier, trailingContent = {
        Checkbox(
            checked = subtopic.checked,
            enabled = false,
            onCheckedChange = null,
            modifier = Modifier
                .padding(8.dp)
                .size(size = 24.dp)
        )
    })
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
        TopicDialog(
            titleRes = R.string.edit_topic,
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
                        imageVector = Icons.Outlined.Share,
                        contentDescription = null
                    )
                }
            )
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
private fun CreateSubtopicFAB(
    onCreate: () -> Unit, modifier: Modifier = Modifier
) {
    FloatingActionButton(onClick = onCreate, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.baseline_add_24),
            contentDescription = stringResource(R.string.create_subtopic),
            modifier = Modifier.size(24.dp)
        )
    }
}

@PreviewDynamicColors
@PreviewLightDark
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
                    imageUri = null
                ), Subtopic(
                    id = 2,
                    topicId = 0,
                    title = "Subtopic 2",
                    description = "Description 2",
                    checked = true,
                    imageUri = null
                ), Subtopic(
                    id = 3,
                    topicId = 0,
                    title = "Subtopic 3",
                    description = "Description 3",
                    checked = false,
                    imageUri = null
                )
            ),
            topic = Topic(
                id = 1, title = "Topic 1", checked = false
            ),
            navigateToSubtopic = {},
            navigateBack = {},
            createSubtopic = { _, _, _ -> },
            deleteTopic = {},
            updateTopic = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoadingScreenPreview() {
    StudyAppTheme {
        SubtopicsScaffold(
            subtopics = null,
            topic = null,
            navigateToSubtopic = {},
            navigateBack = {},
            createSubtopic = { _, _, _ -> },
            deleteTopic = {},
            updateTopic = {},
        )
    }
}
