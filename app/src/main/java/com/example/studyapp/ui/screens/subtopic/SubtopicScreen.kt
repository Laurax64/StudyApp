package com.example.studyapp.ui.screens.subtopic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import coil.compose.AsyncImage
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.components.study.SaveSubtopicDialog
import com.example.studyapp.ui.viewmodels.SubtopicViewModel
@Composable
fun SubtopicScreen(
    subtopicViewModel: SubtopicViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subtopic by subtopicViewModel.subtopic.collectAsStateWithLifecycle(null)
    SubtopicScreen(
        subtopic = subtopic,
        updateSubtopic = subtopicViewModel::updateSubtopic,
        deleteSubtopic = subtopicViewModel::deleteSubtopic,
        modifier = modifier.padding(horizontal = 16.dp),
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubtopicScreen(
    subtopic: Subtopic?,
    updateSubtopic: (Subtopic) -> Unit,
    deleteSubtopic: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val isWidthAtLeastMedium = currentWindowAdaptiveInfo().windowSizeClass
        .isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
    if (subtopic == null) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
    } else {
        if (showDialog) {
            SaveSubtopicDialog(
                titleId = R.string.edit_subtopic,
                onDismiss = { showDialog = false },
                saveSubtopic = { title, description, imageUri ->
                    updateSubtopic(
                        Subtopic(
                            id = subtopic.id,
                            title = title,
                            description = description,
                            checked = subtopic.checked,
                            topicId = subtopic.topicId,
                            bookmarked = subtopic.bookmarked,
                            imageUri = imageUri
                        )
                    )
                    showDialog = false
                },
                modifier = modifier,
                subtopic = subtopic,
                isFullScreenDialog = isWidthAtLeastMedium
            )
        } else {
            Scaffold(
                modifier = modifier,
                topBar = {
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
                    }, actions = {
                        MoreActionsMenu(
                            shareSubtopic = { /* TODO: Implement share functionality */ },
                            editSubtopic = { showDialog = true },
                            deleteSubtopic = {
                                deleteSubtopic()
                                navigateBack()
                            })
                    })
                }) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues = innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AsyncImage(
                        model = subtopic.imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
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
private fun MoreActionsMenu(
    shareSubtopic: () -> Unit,
    editSubtopic: () -> Unit,
    deleteSubtopic: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(modifier, horizontalAlignment = Alignment.End) {
        Icon(
            painter = painterResource(R.drawable.baseline_more_vert_24),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = stringResource(R.string.menu),
            modifier = Modifier.clickable { expanded = true })

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.share)) },
                onClick = { shareSubtopic() },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_share_24),
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
                        contentDescription = stringResource(R.string.open_edit_topic_dialog)
                    )
                })
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                onClick = { deleteSubtopic() },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_delete_outline_24),
                        contentDescription = stringResource(R.string.open_delete_topic_dialog)
                    )
                })
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun SubtopicScreenPreview() {
    SubtopicScreen(
        subtopic = Subtopic(
            id = 1,
            title = "Subtopic Title",
            description = "Subtopic Description",
            checked = false,
            bookmarked = false,
            topicId = 1,
            imageUri = null
        ),
        updateSubtopic = {}, deleteSubtopic = {}, navigateBack = {}
    )
}