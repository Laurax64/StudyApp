package com.example.studyapp.ui.subtopic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.FloatingToolbarDefaults.VibrantFloatingActionButton
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import coil.compose.AsyncImage
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.components.study.SaveSubtopicDialog
import com.example.studyapp.ui.theme.StudyAppTheme

private enum class SubtopicDialog {
    EDIT_SUBTOPIC, DELETE_SUBTOPIC
}

@Composable
fun SubtopicScreen(
    viewModel: SubtopicViewModel,
    navigateBack: () -> Unit,
    navigateToSubtopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SubtopicScreen(
        uiState = uiState,
        updateSubtopic = viewModel::updateSubtopic,
        deleteSubtopic = viewModel::deleteSubtopic,
        modifier = modifier.padding(horizontal = 16.dp),
        navigateToSubtopic = navigateToSubtopic,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SubtopicScreen(
    uiState: SubtopicUiState,
    updateSubtopic: (Subtopic) -> Unit,
    deleteSubtopic: () -> Unit,
    navigateBack: () -> Unit,
    navigateToSubtopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        SubtopicUiState.Loading ->
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingIndicator()
            }

        is SubtopicUiState.Success ->
            SubtopicScaffold(
                uiState = uiState,
                updateSubtopic = updateSubtopic,
                deleteSubtopic = deleteSubtopic,
                navigateBack = navigateBack,
                navigateToSubtopic = navigateToSubtopic,
                modifier = modifier,
            )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SubtopicScaffold(
    uiState: SubtopicUiState.Success,
    updateSubtopic: (Subtopic) -> Unit,
    deleteSubtopic: () -> Unit,
    navigateBack: () -> Unit,
    navigateToSubtopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var dialogType by rememberSaveable { mutableStateOf<SubtopicDialog?>(null) }
    var expanded by rememberSaveable { mutableStateOf(true) }
    val subtopic = uiState.subtopic
    val isScreenWidthCompact =
        !currentWindowAdaptiveInfo().windowSizeClass.isWidthAtLeastBreakpoint(
            WIDTH_DP_MEDIUM_LOWER_BOUND
        )
    dialogType?.let {
        SubtopicDialog(
            subtopic = subtopic,
            deleteSubtopic = {
                // Navigate back because the subtopic is about to be deleted.
                navigateBack()
                deleteSubtopic()
            },
            updateSubtopic = updateSubtopic,
            dismissDialog = { dialogType = null },
            isScreenWidthCompact = isScreenWidthCompact,
            dialogType = it
        )
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            SubtopicTopAppBar(
                subtopic = subtopic,
                navigateBack = navigateBack,
                toggleBookmarked = { updateSubtopic(subtopic.copy(bookmarked = !subtopic.bookmarked)) })
        },
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            SubtopicAnswerCard(
                isScreenWidthCompact = isScreenWidthCompact,
                subtopic = subtopic,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = innerPadding)
                    .padding(horizontal = 16.dp)
            )
            SubtopicToolbar(
                onDelete = { dialogType = SubtopicDialog.DELETE_SUBTOPIC },
                onEdit = { dialogType = SubtopicDialog.EDIT_SUBTOPIC },
                onCheck = { updateSubtopic(subtopic.copy(checked = true)) },
                onNext = { uiState.nextSubtopicId?.let { navigateToSubtopic(it) } },
                onPrevious = { uiState.previousSubtopicId?.let { navigateToSubtopic(it) } },
                expanded = expanded,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = -ScreenOffset, y = -ScreenOffset),
            )
        }
    }
}

@Composable
private fun SubtopicAnswerCard(
    isScreenWidthCompact: Boolean, subtopic: Subtopic, modifier: Modifier = Modifier
) {
    if (isScreenWidthCompact) {
        Column(
            modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = subtopic.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = subtopic.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    } else {
        Row(
            modifier = modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = subtopic.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = subtopic.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SubtopicTopAppBar(
    subtopic: Subtopic,
    navigateBack: () -> Unit,
    toggleBookmarked: () -> Unit,
    modifier: Modifier = Modifier
) {
    LargeFlexibleTopAppBar(
        title = { Text(text = subtopic.title, overflow = Ellipsis) },
        modifier = modifier,
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
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Icon(
                    painter = painterResource(
                        if (subtopic.bookmarked) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
                    ),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable { toggleBookmarked() },
                    contentDescription = stringResource(
                        if (subtopic.bookmarked) R.string.add_bookmark else R.string.remove_bookmark
                    )
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_share_24),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun SubtopicDialog(
    subtopic: Subtopic,
    isScreenWidthCompact: Boolean,
    deleteSubtopic: () -> Unit,
    dismissDialog: () -> Unit,
    dialogType: SubtopicDialog,
    updateSubtopic: (Subtopic) -> Unit,
    modifier: Modifier = Modifier
) {
    when (dialogType) {
        SubtopicDialog.DELETE_SUBTOPIC -> DeleteSubtopicDialog(
            modifier = modifier,
            onDismiss = dismissDialog,
            deleteSubtopic = deleteSubtopic,
            subtopicTitle = subtopic.title
        )

        SubtopicDialog.EDIT_SUBTOPIC -> SaveSubtopicDialog(
            modifier = modifier,
            titleId = R.string.edit_subtopic,
            onDismiss = dismissDialog,
            isFullScreenDialog = isScreenWidthCompact,
            saveSubtopic = { title, description, imageUri ->
                updateSubtopic(
                    subtopic.copy(
                        title = title, description = description, imageUri = imageUri
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
            TextButton(onClick = deleteSubtopic) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } },
        modifier = modifier
    )
}

/**
 * A horizontal floating toolbar for the subtopic screen.
 *
 * @param onDelete the action to perform when the delete button is clicked.
 * @param onEdit the action to perform when the edit button is clicked.
 * @param onCheck the function to check the topic or null if it is already checked.
 * @param onNext the action to perform when the next button is clicked or null if there is no next subtopic.
 * @param onPrevious the action to perform when the previous button is clicked or null if there is no previous subtopic.
 * @param expanded whether the toolbar is expanded or not.
 * @param modifier the modifier to apply to this layout.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SubtopicToolbar(
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onCheck: (() -> Unit)?,
    onNext: (() -> Unit)?,
    onPrevious: (() -> Unit)?,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    HorizontalFloatingToolbar(
        modifier = modifier,
        expanded = expanded,
        colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(),
        floatingActionButton = {
            onCheck?.let {
                VibrantFloatingActionButton(onClick = it) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_check_24),
                        contentDescription = stringResource(R.string.create_subtopic),
                    )
                }
            }
        },
        content = {
            onPrevious?.let {
                IconButton(onClick = it) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_navigate_before_24),
                        contentDescription = stringResource(R.string.go_to_previous_subtopic)
                    )
                }
            }
            onNext?.let {
                IconButton(onClick = it) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_navigate_next_24),
                        contentDescription = stringResource(R.string.go_to_next_subtopic)
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(R.drawable.baseline_delete_24),
                    contentDescription = stringResource(R.string.open_delete_subtopic_dialog)
                )
            }
            IconButton(onClick = onEdit) {
                Icon(
                    painter = painterResource(R.drawable.baseline_edit_24),
                    contentDescription = stringResource(R.string.open_edit_subtopic_dialog)
                )
            }
        }
    )
}

@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun SubtopicScreenPreview() {
    StudyAppTheme {
        SubtopicScreen(
            uiState = SubtopicUiState.Success(
                subtopic = Subtopic(
                    id = 1,
                    title = "Subtopic Title",
                    description = "Subtopic Description",
                    checked = false,
                    bookmarked = false,
                    topicId = 1,
                    imageUri = null,
                    index = 1
                ),
                previousSubtopicId = 0,
                nextSubtopicId = 2,
            ),
            updateSubtopic = {},
            deleteSubtopic = {},
            navigateBack = {},
            navigateToSubtopic = {}
        )
    }
}