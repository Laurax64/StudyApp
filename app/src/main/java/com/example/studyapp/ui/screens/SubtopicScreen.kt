package com.example.studyapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.components.SubtopicFullScreenDialog
import com.example.studyapp.ui.viewmodels.SubtopicViewModel

@Composable
fun SubtopicScreen(
    subtopicViewModel: SubtopicViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subtopic by subtopicViewModel.subtopic.collectAsStateWithLifecycle()
    SubtopicScaffold(
        subtopic = subtopic,
        updateSubtopic = subtopicViewModel::updateSubtopic,
        deleteSubtopic = subtopicViewModel::deleteSubtopic,
        modifier = modifier.padding(horizontal = 16.dp),
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubtopicScaffold(
    subtopic: Subtopic?,
    updateSubtopic: (Subtopic) -> Unit,
    deleteSubtopic: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    if (subtopic == null) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
    } else {
        if (showDialog) {
            SubtopicFullScreenDialog(
                titleRes = R.string.edit_subtopic,
                onDismiss = { showDialog = false },
                saveSubtopic = { title, description, imageUri ->
                    updateSubtopic(
                        Subtopic(
                            id = subtopic.id,
                            title = title,
                            description = description,
                            checked = subtopic.checked,
                            topicId = subtopic.topicId,
                            imageUri = imageUri
                        )
                    )
                    showDialog = false
                },
                modifier = modifier,
                subtopic = subtopic,
            )
        } else {
            Scaffold(
                modifier = modifier,
                topBar =
                    {
                        TopAppBar(
                            title = {
                                Text(
                                    text = subtopic.title,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            },
                            navigationIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = stringResource(R.string.go_back_to_subtopics),
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { navigateBack() })
                            },
                            actions = {
                                MoreActionsMenu(
                                    shareSubtopic = { /* TODO: Implement share functionality */ },
                                    editSubtopic = { showDialog = true },
                                    deleteSubtopic = {
                                        deleteSubtopic()
                                        navigateBack()
                                    }
                                )
                            }
                        )
                    }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues = innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SubtopicImage(
                        modifier = Modifier.fillMaxWidth(),
                        imageUri = subtopic.imageUri,
                    )
                    Text(
                        text = subtopic.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun SubtopicImage(
    modifier: Modifier = Modifier,
    imageUri: String?,
) {
    if (imageUri != null) {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = modifier
        )
    }
}

@Composable
private fun MoreActionsMenu(
    shareSubtopic: () -> Unit,
    editSubtopic: () -> Unit,
    deleteSubtopic: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(modifier, horizontalAlignment = Alignment.End) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.menu),
            modifier = Modifier.clickable { expanded = true })

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.share)) },
                onClick = { shareSubtopic() },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Share, // TODO replace with material 3 icon
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = null
                    )
                })
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit)) },
                onClick = editSubtopic,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_create_24),
                        contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                onClick = { deleteSubtopic() },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_delete_outline_24),
                        contentDescription = null // TODO: Add content description
                    )
                })
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun SubtopicScreenPreview() {
    SubtopicScaffold(
        subtopic = Subtopic(
            id = 1,
            title = "Subtopic Title",
            description = "Subtopic Description",
            checked = false,
            topicId = 1,
            imageUri = null
        ),
        updateSubtopic = {},
        deleteSubtopic = {},
        navigateBack = {}
    )
}