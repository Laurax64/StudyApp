package com.example.studyapp.ui.screens.subtopics

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
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
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.components.study.SaveSubtopicDialog
import com.example.studyapp.ui.components.study.SaveTopicDialog
import com.example.studyapp.ui.screens.topics.ScrollableTopicsList

private enum class DialogType {
    EDIT_TOPIC,
    DELETE_TOPIC,
    CREATE_SUBTOPIC,
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SubtopicsScaffold(
    subtopics: List<Subtopic>,
    topics: List<Topic>,
    topic: Topic,
    saveSubtopic: (String, String, String?) -> Unit,
    deleteTopic: () -> Unit,
    updateTopic: (Topic) -> Unit,
    navigateToSubtopic: (Int) -> Unit,
    navigateToTopic: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    val paneAdaptedValue = scaffoldNavigator.scaffoldState.currentState.primary
    var showSearchBar by rememberSaveable { mutableStateOf(false) }
    var dialogType by rememberSaveable { mutableStateOf<DialogType?>(null) }
    if (dialogType == DialogType.CREATE_SUBTOPIC && paneAdaptedValue == PaneAdaptedValue.Hidden) {
        SaveSubtopicDialog(
            titleId = R.string.create_subtopic,
            onDismiss = { dialogType = null },
            isFullScreenDialog = true,
            modifier = modifier,
            saveSubtopic = { title, description, imageUri ->
                saveSubtopic(title, description, imageUri)
            },
        )
    } else {
        dialogType?.let {
            Dialog(
                topic = topic,
                updateTopic = updateTopic,
                deleteTopic = deleteTopic,
                navigateBack = navigateBack,
                dismissDialog = { dialogType = null },
                dialogType = it,
                saveSubtopic = saveSubtopic
            )
        }
        Scaffold(
            modifier = modifier,
            topBar = {
                if (!showSearchBar) {
                    SubtopicsTopAppBar(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        topic = topic,
                        onDeleteTopic = { dialogType = DialogType.DELETE_TOPIC },
                        onEditTopic = { dialogType = DialogType.EDIT_TOPIC },
                        onSearch = { showSearchBar = true },
                        navigateBack = navigateBack
                    )
                }
            },
            floatingActionButton = {
                CreateSubtopicFAB(onCreate = {
                    dialogType = DialogType.CREATE_SUBTOPIC
                })
            },
        ) { innerPadding ->
            SubtopicsNavigableListDetailPaneScaffold(
                scaffoldNavigator = scaffoldNavigator,
                paneAdaptedValue = paneAdaptedValue,
                subtopics = subtopics,
                topics = topics,
                topic = topic,
                navigateToSubtopic = navigateToSubtopic,
                navigateToTopic = navigateToTopic,
                showSearchBar = showSearchBar,
                closeSearchBar = { showSearchBar = false },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun Dialog(
    topic: Topic,
    updateTopic: (Topic) -> Unit,
    deleteTopic: () -> Unit,
    navigateBack: () -> Unit,
    dismissDialog: () -> Unit,
    dialogType: DialogType,
    saveSubtopic: (String, String, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    when (dialogType) {
        DialogType.EDIT_TOPIC ->
            SaveTopicDialog(
                modifier = modifier,
                topic = topic,
                onDismiss = dismissDialog,
                onSave = {
                    updateTopic(it)
                    dismissDialog()
                }
            )

        DialogType.DELETE_TOPIC ->
            DeleteTopicDialog(
                modifier = modifier,
                onDismiss = dismissDialog,
                deleteTopic = {
                    // Navigate back because the topic is about to be deleted.
                    navigateBack()
                    deleteTopic()
                },
                topicTitle = topic.title
            )

        DialogType.CREATE_SUBTOPIC ->
            SaveSubtopicDialog(
                modifier = modifier,
                titleId = R.string.create_subtopic,
                onDismiss = dismissDialog,
                isFullScreenDialog = false,
                saveSubtopic = saveSubtopic
            )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun <T> SubtopicsNavigableListDetailPaneScaffold(
    scaffoldNavigator: ThreePaneScaffoldNavigator<T>,
    paneAdaptedValue: PaneAdaptedValue,
    subtopics: List<Subtopic>,
    topics: List<Topic>,
    topic: Topic,
    navigateToSubtopic: (Int) -> Unit,
    navigateToTopic: (Int) -> Unit,
    showSearchBar: Boolean,
    closeSearchBar: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                if (paneAdaptedValue == PaneAdaptedValue.Expanded) {
                    ScrollableTopicsList(
                        topics = topics,
                        navigateToTopic = navigateToTopic,
                        selectedTopicId = topic.id,
                    )
                } else {
                    if (paneAdaptedValue == PaneAdaptedValue.Hidden) {
                        SubtopicsPaneContent(
                            subtopics = subtopics,
                            navigateToSubtopic = navigateToSubtopic,
                            closeSearchBar = closeSearchBar,
                            showSearchBar = showSearchBar,
                            topicTitle = topic.title
                        )
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                AnimatedContent(
                    targetState = subtopics,
                    label = "SubtopicsContent"
                ) { animatedSubtopics ->
                    SubtopicsPaneContent(
                        subtopics = animatedSubtopics,
                        navigateToSubtopic = navigateToSubtopic,
                        closeSearchBar = closeSearchBar,
                        showSearchBar = showSearchBar,
                        topicTitle = topic.title
                    )
                }
            }
        },
        modifier = modifier
    )
}


@Composable
private fun DeleteTopicDialog(
    modifier: Modifier = Modifier,
    topicTitle: String,
    deleteTopic: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_topic_dialog_title)) },
        text = { Text(stringResource(R.string.delete_topic_dialog_description, topicTitle)) },
        confirmButton = {
            TextButton(
                onClick = {
                    deleteTopic()
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SubtopicsTopAppBar(
    modifier: Modifier = Modifier,
    topic: Topic,
    onDeleteTopic: () -> Unit,
    onEditTopic: () -> Unit,
    onSearch: () -> Unit,
    navigateBack: () -> Unit
) {
    LargeFlexibleTopAppBar(
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
                    modifier = Modifier.clickable { onSearch() },
                    contentDescription = stringResource(R.string.subtopics_search),
                )
                MoreActionsMenu(
                    onDelete = onDeleteTopic,
                    onEdit = onEditTopic,
                )
            }
        },
        title = { Text(text = topic.title, overflow = Ellipsis) },
        modifier = modifier
    )
}

@Composable
private fun MoreActionsMenu(
    onDelete: () -> Unit,
    onEdit: () -> Unit,
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
                onClick = { /* TODO Implement */ },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_share_24),
                        contentDescription = null
                    )
                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit)) },
                onClick = onEdit,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_create_24),
                        contentDescription = stringResource(R.string.edit_topic)
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                onClick = onDelete,
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
private fun CreateSubtopicFAB(onCreate: () -> Unit, modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        onClick = onCreate,
        modifier = modifier,
        icon = {
            Icon(
                painter = painterResource(R.drawable.baseline_add_24),
                contentDescription = stringResource(R.string.create_subtopic),
            )
        },
        text = { Text(text = stringResource(R.string.create_subtopic)) },
    )
}