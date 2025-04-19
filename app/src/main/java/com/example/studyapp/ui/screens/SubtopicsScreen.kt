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
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.material3.TextField
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
import com.example.studyapp.ui.components.FullScreenDialog
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
        navigateToSubtopic = navigateToSubtopic,
        navigateBack = navigateBack,
        updateChecked = subtopicsViewModel::updateChecked,
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
    updateChecked: (Subtopic, Boolean) -> Unit,
    navigateToSubtopic: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (topic == null || subtopics == null) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        var showDialog by rememberSaveable { mutableStateOf(false) }
        if (showDialog) {
            CreateSubtopicFullScreenDialog(
                onDismiss = { showDialog = false },
                createSubtopic = { title, description, imageUri ->
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
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_search_24),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .clickable {/*TODO*/ }
                                    .size(24.dp),
                                contentDescription = stringResource(R.string.subtopics_search),
                            )
                            MoreActionsMenu()
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
                LazyColumn(Modifier.padding(paddingValues = innerPadding)) {
                    items(subtopics.size) { index ->
                        val subtopic = subtopics[index]
                        SubtopicListItem(
                            subtopic = subtopic,
                            updateChecked = { checked -> updateChecked(subtopic, checked) },
                            modifier = Modifier.clickable { navigateToSubtopic(subtopic.id) })
                    }

                }
            }
        }
    }
}

@Composable
private fun SubtopicListItem(
    subtopic: Subtopic, updateChecked: (Boolean) -> Unit, modifier: Modifier
) {
    ListItem(headlineContent = { Text(subtopic.title) }, modifier = modifier, trailingContent = {
        Checkbox(
            checked = subtopic.checked,
            onCheckedChange = updateChecked,
            modifier = Modifier
                .padding(8.dp)
                .size(size = 24.dp)
        )
    })
}

@Composable
private fun MoreActionsMenu(modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(modifier, horizontalAlignment = Alignment.End) {
        Icon(
            imageVector = Icons.Default.MoreVert,
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
                text = { Text(stringResource(R.string.delete)) },
                onClick = { /* TODO Implement */ },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Delete, contentDescription = null
                    )
                })
        }
    }
}

@Composable
private fun CreateSubtopicFAB(
    onCreate: () -> Unit, modifier: Modifier = Modifier
) {
    FloatingActionButton(onClick = onCreate, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.baseline_add_24),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            contentDescription = stringResource(R.string.create_subtopic),
            modifier = Modifier.size(24.dp)
        )
    }
}


@Composable
private fun CreateSubtopicFullScreenDialog(
    onDismiss: () -> Unit,
    createSubtopic: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf("") }
    var saveData by rememberSaveable { mutableStateOf(false) }
    if (saveData) {
        createSubtopic(title, description, imageUri)
    }
    FullScreenDialog(
        titleRes = R.string.create_subtopic,
        onDismiss = onDismiss,
        onConfirm = { saveData = true },
        modifier = modifier.padding(horizontal = 24.dp)
    ) { innerPadding ->
        SubtopicInputFields(
            updateTitle = { title = it },
            updateDescription = { description = it },
            updateImageUri = { imageUri = it },
            pickImageUri = { /* TODO implement */ "" },
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun SubtopicInputFields(
    updateTitle: (String) -> Unit,
    updateDescription: (String) -> Unit,
    updateImageUri: (String) -> Unit,
    pickImageUri: () -> String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextField(
            value = "",
            onValueChange = updateTitle,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.title)) })
        TextField(
            value = "",
            onValueChange = updateDescription,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.description)) })
        TextField(
            value = "",
            onValueChange = updateImageUri,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.outline_image_24),
                    contentDescription = stringResource(R.string.image),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { updateImageUri(pickImageUri()) })
            },
            label = { Text(stringResource(R.string.image)) })
    }
}

/* TODO add missing fields */
@Composable
private fun CreateSubtopicDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { },
    onCreate: (String, String, String?) -> Unit
) {
    var subtopicTitle by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.create_subtopic)) },
        text = {
            TextField(
                value = subtopicTitle,
                onValueChange = { subtopicTitle = it }, // Update the state with new value
                label = { Text(stringResource(R.string.title)) })
        },
        confirmButton = {
            TextButton(
                onClick = {
                    //   onCreate(title = subtopicTitle, description = ) TODO
                    onDismiss()
                }) {
                Text(stringResource(R.string.create))
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


@PreviewDynamicColors
@PreviewLightDark
@Composable
private fun SubtopicsScreenPreview() {
    StudyAppTheme {
        SubtopicsScaffold(
            subtopics = listOf(
                Subtopic(
                    id = 1,
                    title = "Subtopic 1",
                    description = "Description 1",
                    checked = false,
                    imageUri = null
                ), Subtopic(
                    id = 2,
                    title = "Subtopic 2",
                    description = "Description 2",
                    checked = true,
                    imageUri = null
                ), Subtopic(
                    id = 3,
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
            updateChecked = { _, _ -> })
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
            updateChecked = { _, _ -> })
    }
}
