package com.example.studyapp.ui.screens.subtopics

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
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.screens.subtopics.dialogs.DeleteTopicDialog
import com.example.studyapp.ui.screens.subtopics.dialogs.EditTopicDialog
import com.example.studyapp.ui.screens.topics.ScrollableTopicsList


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SubtopicsScaffold(
    subtopics: List<Subtopic>,
    topics: List<Topic>,
    topic: Topic,
    onCreateSubtopic: () -> Unit,
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
    Scaffold(
        modifier = modifier,
        topBar = {
            if (!showSearchBar) {
                SubtopicsTopAppBar(
                    topic = topic,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    deleteTopic = deleteTopic,
                    updateTopic = updateTopic,
                    onSearch = { showSearchBar = true },
                    navigateBack = navigateBack
                )
            }
        },
        floatingActionButton = { CreateSubtopicFAB(onCreate = onCreateSubtopic) },
    ) { innerPadding ->
        NavigableListDetailPaneScaffold(
            navigator = scaffoldNavigator,
            listPane = {
                AnimatedPane {
                    if (paneAdaptedValue == PaneAdaptedValue.Expanded) {
                        ScrollableTopicsList(
                            topics = topics,
                            navigateToTopic = navigateToTopic,
                            selectedTopicId = topic.id,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                        )
                    } else {
                        if (paneAdaptedValue == PaneAdaptedValue.Hidden) {
                            SubtopicsPaneContent(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                subtopics = subtopics,
                                navigateToSubtopic = navigateToSubtopic,
                                closeSearchBar = { showSearchBar = false },
                                showSearchBar = showSearchBar,
                                topicTitle = topic.title
                            )
                        }
                    }
                }
            },
            detailPane = {
                AnimatedPane {
                    SubtopicsPaneContent(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        subtopics = subtopics,
                        navigateToSubtopic = navigateToSubtopic,
                        closeSearchBar = { showSearchBar = false },
                        showSearchBar = showSearchBar,
                        topicTitle = topic.title
                    )
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SubtopicsTopAppBar(
    modifier: Modifier = Modifier,
    topic: Topic,
    deleteTopic: () -> Unit,
    updateTopic: (Topic) -> Unit,
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