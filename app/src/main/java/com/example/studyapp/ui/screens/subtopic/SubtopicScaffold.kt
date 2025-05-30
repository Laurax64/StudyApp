package com.example.studyapp.ui.screens.subtopic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic

@Composable
internal fun SubtopicScaffold(
    subtopic: Subtopic,
    updateSubtopic: (Subtopic) -> Unit,
    deleteSubtopic: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var dialogType by rememberSaveable { mutableStateOf<DialogType?>(null) }
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
                onDeleteSubtopic = { dialogType = DialogType.DELETE_SUBTOPIC },
                onEditSubtopic = { dialogType = DialogType.EDIT_SUBTOPIC },
                navigateBack = navigateBack,
                modifier = Modifier.padding(horizontal = 16.dp),
                toggleBookmarked = { updateSubtopic(subtopic.copy(bookmarked = !subtopic.bookmarked)) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { updateSubtopic(subtopic.copy(checked = !subtopic.checked)) },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (subtopic.checked) R.drawable.outline_check_box_24 else R.drawable.baseline_check_box_outline_blank_24
                        ),
                        contentDescription = stringResource(
                            if (subtopic.checked) R.string.checked else R.string.unchecked
                        )
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.toggle_checked)
                    )
                }
            )
        }
    ) { innerPadding ->
        SubtopicAnswerCard(
            isScreenWidthCompact = isScreenWidthCompact,
            subtopic = subtopic,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding)
                .padding(horizontal = 16.dp)
        )
    }
}


@Composable
private fun MoreActionsMenu(
    shareSubtopic: () -> Unit,
    onDeleteSubtopic: () -> Unit,
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
                text = { Text(stringResource(R.string.delete)) },
                onClick = { onDeleteSubtopic() },
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SubtopicTopAppBar(
    subtopic: Subtopic,
    onDeleteSubtopic: () -> Unit,
    navigateBack: () -> Unit,
    onEditSubtopic: () -> Unit,
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
                    painter = painterResource(R.drawable.outline_create_24),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable { onEditSubtopic() },
                    contentDescription = stringResource(R.string.open_edit_subtopic_dialog),
                )
                MoreActionsMenu(
                    shareSubtopic = { /* TODO: Implement share functionality */ },
                    onDeleteSubtopic = onDeleteSubtopic
                )
            }
        }
    )
}
