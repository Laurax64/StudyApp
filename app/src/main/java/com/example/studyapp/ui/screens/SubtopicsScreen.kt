package com.example.studyapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.SubtopicsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtopicsScreen(
    subtopicsViewModel: SubtopicsViewModel,
    navigateToSubtopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    SubtopicsScaffold(
        subtopics = subtopicsViewModel.subtopics.collectAsState().value,
        createSubtopic = { title, description, imageUri ->
            subtopicsViewModel.createSubtopic(
                title = title,
                description = description,
                imageUri = imageUri
            )
        },
        navigateToSubtopic = navigateToSubtopic,
        updateChecked = subtopicsViewModel::updateChecked,
        modifier = modifier
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubtopicsScaffold(
    subtopics: List<Subtopic>,
    createSubtopic: (String, String, String?) -> Unit,
    updateChecked: (Subtopic, Boolean) -> Unit,
    navigateToSubtopic: (Int) -> Unit,
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
                        contentDescription = stringResource(R.string.subtopics_search),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                            }
                    )
                },
                actions = { MoreActionsMenu() },
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        floatingActionButton = {
            CreateSubtopicFAB(onCreate = createSubtopic)
        }
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            items(subtopics.size) { index ->
                val subtopic = subtopics[index]
                SubtopicListItem(
                    subtopic = subtopic,
                    updateChecked = { checked -> updateChecked(subtopic, checked) },
                    modifier = Modifier.clickable { navigateToSubtopic(subtopic.id) }
                )
            }
        }
    }
}

@Composable
private fun SubtopicListItem(
    subtopic: Subtopic,
    updateChecked: (Boolean) -> Unit,
    modifier: Modifier
) {
    ListItem(
        headlineContent = { Text(subtopic.title) },
        modifier = modifier,
        trailingContent = {
            Checkbox(
                checked = subtopic.checked,
                onCheckedChange = updateChecked,
                modifier = Modifier.padding(8.dp)
            )
        }
    )
}

@Composable
private fun MoreActionsMenu(modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(modifier, horizontalAlignment = Alignment.End) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.menu),
            modifier = Modifier.clickable { expanded = true }
        )

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
                text = { Text(stringResource(R.string.delete)) },
                onClick = { /* TODO Implement */ },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
private fun CreateSubtopicFAB(
    onCreate: (String, String, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    FloatingActionButton(onClick = { showDialog = true }, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.baseline_add_24),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            contentDescription = stringResource(R.string.create_subtopic),
            modifier = Modifier.size(24.dp)
        )
    }
    if (showDialog) {
        // ImagePicker()
        CreateSubtopicDialog(
            onDismiss = { showDialog = false },
            onCreate = onCreate
        )
    }
}

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
                label = { Text(stringResource(R.string.title)) }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    //   onCreate(title = subtopicTitle, description = ) TODO
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


@PreviewDynamicColors
@PreviewLightDark
@Composable
private fun SubtopicScreenPreview() {
    StudyAppTheme {
        SubtopicsScaffold(
            subtopics = listOf(
                Subtopic(
                    id = 1,
                    title = "Subtopic 1",
                    description = "Description 1",
                    checked = false,
                    imageUri = null
                ),
                Subtopic(
                    id = 2,
                    title = "Subtopic 2",
                    description = "Description 2",
                    checked = true,
                    imageUri = null
                ),
                Subtopic(
                    id = 3,
                    title = "Subtopic 3",
                    description = "Description 3",
                    checked = false,
                    imageUri = null
                )
            ),
            navigateToSubtopic = {},
            createSubtopic = { _, _, _ -> },
            updateChecked = { _, _ -> }
        )
    }
}
