package com.example.studyapp.ui.screens.subtopic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.components.study.SaveSubtopicDialog


private enum class DialogType {
    EDIT_SUBTOPIC,
    DELETE_SUBTOPIC
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SubtopicScaffold(
    subtopic: Subtopic,
    updateSubtopic: (Subtopic) -> Unit,
    deleteSubtopic: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    val paneAdaptedValue = scaffoldNavigator.scaffoldState.currentState.primary
    var dialogType by rememberSaveable { mutableStateOf<DialogType?>(null) }

    if (dialogType == DialogType.EDIT_SUBTOPIC && paneAdaptedValue == PaneAdaptedValue.Hidden) {
        SaveSubtopicDialog(
            titleId = R.string.edit_subtopic,
            onDismiss = { dialogType = null },
            isFullScreenDialog = true,
            modifier = modifier,
            saveSubtopic = { title, description, imageUri ->
                updateSubtopic(
                    subtopic.copy(
                        title = title,
                        description = description,
                        imageUri = imageUri
                    )
                )
            },
            subtopic = subtopic
        )
    } else {
        dialogType?.let {
            Dialog(
                subtopic = subtopic,
                deleteSubtopic = deleteSubtopic,
                updateSubtopic = updateSubtopic,
                navigateBack = navigateBack,
                dismissDialog = { dialogType = null },
                dialogType = it
            )
        }
        Scaffold(
            modifier = modifier,
            topBar = {
                LargeTopAppBar(
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
                            editSubtopic = { dialogType = DialogType.EDIT_SUBTOPIC },
                            deleteSubtopic = {
                                deleteSubtopic()
                                navigateBack()
                            }
                        )
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { updateSubtopic(subtopic.copy(checked = !subtopic.checked)) },
                    modifier = modifier,
                    icon = {
                        Icon(
                            painter = painterResource(
                                if (subtopic.checked) R.drawable.outline_check_box_24 else R.drawable.baseline_check_box_outline_blank_24
                            ),
                            contentDescription = stringResource(
                                if (subtopic.checked) R.string.uncheck_subtopic else R.string.check_subtopic
                            )
                        )
                    },
                    text = { Text(text = stringResource(R.string.create_subtopic)) },
                )
            }
        ) { innerPadding ->
            if (paneAdaptedValue == PaneAdaptedValue.Hidden) {
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
            } else {
                Row(
                    modifier = Modifier
                        .padding(paddingValues = innerPadding)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AsyncImage(
                        model = subtopic.imageUri,
                        contentDescription = null,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = subtopic.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
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
                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit)) },
                onClick = editSubtopic,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_create_24),
                        contentDescription = stringResource(R.string.open_edit_topic_dialog)
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                onClick = { deleteSubtopic() },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_delete_outline_24),
                        contentDescription = stringResource(R.string.open_delete_topic_dialog)
                    )
                }
            )
        }
    }
}

@Composable
private fun Dialog(
    subtopic: Subtopic,
    deleteSubtopic: () -> Unit,
    navigateBack: () -> Unit,
    dismissDialog: () -> Unit,
    dialogType: DialogType,
    updateSubtopic: (Subtopic) -> Unit,
    modifier: Modifier = Modifier
) {
    when (dialogType) {
        DialogType.DELETE_SUBTOPIC ->
            DeleteSubtopicDialog(
                modifier = modifier,
                onDismiss = dismissDialog,
                deleteSubtopic = {
                    // Navigate back because the topic is about to be deleted.
                    navigateBack()
                    deleteSubtopic()
                },
                subtopicTitle = subtopic.title
            )

        DialogType.EDIT_SUBTOPIC ->
            SaveSubtopicDialog(
                modifier = modifier,
                titleId = R.string.edit_subtopic,
                onDismiss = dismissDialog,
                isFullScreenDialog = false,
                saveSubtopic = { title, description, imageUri ->
                    updateSubtopic(
                        subtopic.copy(
                            title = title,
                            description = description,
                            imageUri = imageUri
                        )
                    )
                },
                subtopic = subtopic
            )
    }
}

@Composable
private fun DeleteSubtopicDialog(
    modifier: Modifier = Modifier,
    subtopicTitle: String,
    deleteSubtopic: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_subtopic_dialog_title)) },
        text = { Text(stringResource(R.string.delete_subtopic_dialog_description, subtopicTitle)) },
        confirmButton = {
            TextButton(
                onClick = {
                    deleteSubtopic()
                    onDismiss()
                }
            )
            {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } },
        modifier = modifier
    )
}
