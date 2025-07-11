package com.example.studyapp.ui.subtopics

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.FloatingToolbarDefaults.StandardFloatingActionButton
import androidx.compose.material3.FloatingToolbarDefaults.floatingToolbarVerticalNestedScroll
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
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
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.ui.components.DockedSearchBar
import com.example.studyapp.ui.components.PlaceholderColumn
import com.example.studyapp.ui.components.study.LoadingIndicatorBox
import com.example.studyapp.ui.components.study.SaveSubtopicDialog
import com.example.studyapp.ui.components.study.SaveTopicDialog
import com.example.studyapp.ui.components.study.TopicsLazyColumn
import com.example.studyapp.ui.theme.StudyAppTheme
import kotlinx.coroutines.launch


private enum class SubtopicsDialogType {
    EDIT_TOPIC,
    DELETE_TOPIC,
    CREATE_SUBTOPIC,
}

@Composable
internal fun SubtopicsScreen(
    subtopicsViewModel: SubtopicsViewModel,
    navigateToSubtopic: (Int) -> Unit,
    navigateToTopic: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by subtopicsViewModel.uiState.collectAsStateWithLifecycle()
    SubtopicsScreen(
        uiState = uiState,
        saveSubtopic = subtopicsViewModel::addSubtopic,
        deleteTopic = subtopicsViewModel::deleteTopic,
        updateTopic = subtopicsViewModel::updateTopic,
        navigateToSubtopic = navigateToSubtopic,
        navigateToTopic = navigateToTopic,
        navigateBack = navigateBack,
        modifier = modifier
    )
}

@VisibleForTesting
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SubtopicsScreen(
    uiState: SubtopicsUiState,
    saveSubtopic: (Subtopic) -> Unit,
    deleteTopic: () -> Unit,
    updateTopic: (Topic) -> Unit,
    navigateToSubtopic: (Int) -> Unit,
    navigateToTopic: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        SubtopicsUiState.Loading ->
            LoadingIndicatorBox()

        is SubtopicsUiState.Success ->
            SubtopicsScaffold(
                uiState = uiState,
                navigateToSubtopic = navigateToSubtopic,
                navigateToTopic = navigateToTopic,
                saveSubtopic = saveSubtopic,
                deleteTopic = deleteTopic,
                updateTopic = updateTopic,
                navigateBack = navigateBack,
                modifier = modifier.fillMaxWidth()
            )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SubtopicsScaffold(
    uiState: SubtopicsUiState.Success,
    saveSubtopic: (Subtopic) -> Unit,
    deleteTopic: () -> Unit,
    updateTopic: (Topic) -> Unit,
    navigateToSubtopic: (Int) -> Unit,
    navigateToTopic: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val topic = uiState.selectedTopic
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    val paneAdaptedValue = scaffoldNavigator.scaffoldState.currentState.primary
    var showSearchBar by rememberSaveable { mutableStateOf(false) }
    var dialogType by rememberSaveable { mutableStateOf<SubtopicsDialogType?>(null) }
    var expanded by rememberSaveable { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val isScreenWidthCompact =
        !currentWindowAdaptiveInfo().windowSizeClass.isWidthAtLeastBreakpoint(
            WIDTH_DP_MEDIUM_LOWER_BOUND
        )
    if (dialogType == SubtopicsDialogType.CREATE_SUBTOPIC && isScreenWidthCompact) {
        SaveSubtopicDialog(
            onDismiss = { dialogType = null },
            isFullScreenDialog = true,
            modifier = modifier,
            topicId = topic.id,
            saveSubtopic = saveSubtopic
        )
    } else {
        dialogType?.let {
            SubtopicsDialog(
                topic = topic,
                updateTopic = updateTopic,
                deleteTopic = {
                    deleteTopic()
                    navigateBack()
                },
                dismissDialog = { dialogType = null },
                dialogType = it,
                saveSubtopic = { subtopic ->
                    coroutineScope.launch {
                        saveSubtopic(subtopic)
                    }
                }
            )
        }
        Scaffold(
            modifier = modifier,
            topBar = {
                if (!showSearchBar) {
                    SubtopicsAppBar(
                        topicTitle = topic.title,
                        onSearch = { showSearchBar = true },
                        onShare = {},
                        navigateBack = navigateBack
                    )
                }
            },
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                SubtopicsNavigableListDetailPaneScaffold(
                    scaffoldNavigator = scaffoldNavigator,
                    paneAdaptedValue = paneAdaptedValue,
                    uiState = uiState,
                    navigateToSubtopic = navigateToSubtopic,
                    navigateToTopic = navigateToTopic,
                    showSearchBar = showSearchBar,
                    closeSearchBar = { showSearchBar = false },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .floatingToolbarVerticalNestedScroll(
                            expanded = expanded,
                            onExpand = { expanded = true },
                            onCollapse = { expanded = false }
                        )
                )
                SubtopicsToolbar(
                    onDelete = { dialogType = SubtopicsDialogType.DELETE_TOPIC },
                    onEdit = { dialogType = SubtopicsDialogType.EDIT_TOPIC },
                    onCreate = { dialogType = SubtopicsDialogType.CREATE_SUBTOPIC },
                    expanded = expanded,
                    modifier =
                        Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = -ScreenOffset, y = -ScreenOffset),
                )
            }
        }
    }
}

@Composable
private fun SubtopicsDialog(
    topic: Topic,
    updateTopic: (Topic) -> Unit,
    deleteTopic: () -> Unit,
    dismissDialog: () -> Unit,
    dialogType: SubtopicsDialogType,
    saveSubtopic: (Subtopic) -> Unit,
    modifier: Modifier = Modifier
) {
    when (dialogType) {
        SubtopicsDialogType.EDIT_TOPIC ->
            SaveTopicDialog(
                modifier = modifier,
                topic = topic,
                onDismiss = dismissDialog,
                onSave = {
                    updateTopic(it)
                    dismissDialog()
                }
            )

        SubtopicsDialogType.DELETE_TOPIC ->
            DeleteTopicDialog(
                modifier = modifier,
                onDismiss = dismissDialog,
                deleteTopic = deleteTopic,
                topicTitle = topic.title
            )

        SubtopicsDialogType.CREATE_SUBTOPIC ->
            SaveSubtopicDialog(
                modifier = modifier,
                onDismiss = dismissDialog,
                isFullScreenDialog = false,
                saveSubtopic = saveSubtopic,
                topicId = topic.id
            )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun <T> SubtopicsNavigableListDetailPaneScaffold(
    scaffoldNavigator: ThreePaneScaffoldNavigator<T>,
    paneAdaptedValue: PaneAdaptedValue,
    uiState: SubtopicsUiState.Success,
    navigateToSubtopic: (Int) -> Unit,
    navigateToTopic: (Int) -> Unit,
    showSearchBar: Boolean,
    closeSearchBar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topic = uiState.selectedTopic
    val subtopics = uiState.subtopics
    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                if (paneAdaptedValue == PaneAdaptedValue.Expanded) {
                    TopicsLazyColumn(
                        topicsWithProgress = uiState.topicsWithProgress,
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
                            topicTitle = topic.title,
                        )
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                AnimatedContent(targetState = subtopics) {
                    SubtopicsPaneContent(
                        subtopics = it,
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
private fun SubtopicsAppBar(
    modifier: Modifier = Modifier,
    topicTitle: String,
    onSearch: () -> Unit,
    onShare: () -> Unit,
    navigateBack: () -> Unit
) {
    LargeFlexibleTopAppBar(
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(R.string.go_back_to_topics),
                )
            }
        },
        actions = {
            IconButton(onClick = onSearch) {
                Icon(
                    painter = painterResource(R.drawable.baseline_search_24),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = stringResource(R.string.subtopics_search),
                )
            }
            IconButton(onClick = onShare) {
                Icon(
                    painter = painterResource(R.drawable.baseline_share_24),
                    contentDescription = null
                )
            }
        },
        title = { Text(text = topicTitle, overflow = Ellipsis) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SubtopicsToolbar(
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onCreate: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean
) {
    HorizontalFloatingToolbar(
        modifier = modifier.testTag("SubtopicsToolbar"),
        expanded = expanded,
        floatingActionButton = {
            StandardFloatingActionButton(onClick = onCreate) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_24),
                    contentDescription = stringResource(R.string.create_subtopic),
                )
            }
        },
        content = {
            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(R.drawable.baseline_delete_24),
                    contentDescription = stringResource(R.string.open_delete_topic_dialog)
                )
            }
            IconButton(onClick = onEdit) {
                Icon(
                    painter = painterResource(R.drawable.baseline_edit_24),
                    contentDescription = stringResource(R.string.open_edit_topic_dialog)
                )
            }
        }
    )
}

@Composable
fun SubtopicsPaneContent(
    modifier: Modifier = Modifier,
    subtopics: List<Subtopic>?,
    navigateToSubtopic: (Int) -> Unit,
    closeSearchBar: () -> Unit,
    showSearchBar: Boolean,
    topicTitle: String
) {
    if (subtopics == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        if (showSearchBar) {
            SubtopicsSearchBar(
                modifier = modifier,
                navigateToSubtopic = navigateToSubtopic,
                closeSearchBar = closeSearchBar,
                subtopics = subtopics,
                topicTitle = topicTitle
            )
        } else {
            FilterableSubtopicsColumn(
                subtopics = subtopics,
                navigateToSubtopic = navigateToSubtopic,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun FilterableSubtopicsColumn(
    subtopics: List<Subtopic>,
    navigateToSubtopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showOnlyNotChecked by rememberSaveable { mutableStateOf(false) }
    var showOnlyBookmarked by rememberSaveable { mutableStateOf(false) }
    val filteredSubtopics = subtopics.filter {
        !(it.checked && showOnlyNotChecked) && (it.bookmarked || !showOnlyBookmarked)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = rememberScrollState()
    // Hide keyboard when scrolling
    if (state.isScrollInProgress) {
        keyboardController?.hide()
    }
    Column(modifier = modifier) {
        if (subtopics.isNotEmpty()) {
            FilteredChipsRow(
                showOnlyNotChecked = showOnlyNotChecked,
                toggleShowOnlyNotChecked = { showOnlyNotChecked = !showOnlyNotChecked },
                showOnlyBookmarked = showOnlyBookmarked,
                toggleShowOnlyBookmarked = { showOnlyBookmarked = !showOnlyBookmarked },
                modifier = Modifier.fillMaxWidth()
            )
            SubtopicsLazyColumn(
                filteredSubtopics = filteredSubtopics,
                navigateToSubtopic = navigateToSubtopic,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            PlaceholderColumn(
                textId = R.string.no_subtopics_exist,
                iconId = R.drawable.outline_subtitles_24,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Composable
private fun SubtopicsSearchBar(
    modifier: Modifier = Modifier,
    navigateToSubtopic: (Int) -> Unit,
    subtopics: List<Subtopic>,
    topicTitle: String,
    closeSearchBar: () -> Unit,
) {
    DockedSearchBar(
        modifier = modifier,
        items = subtopics,
        closeSearchBar = closeSearchBar,
        itemLabel = { it.title },
        placeholderText = stringResource(R.string.search_in, topicTitle),
    ) {
        FilterableSubtopicsColumn(
            subtopics = it,
            navigateToSubtopic = navigateToSubtopic,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun FilteredChipsRow(
    showOnlyNotChecked: Boolean,
    toggleShowOnlyNotChecked: () -> Unit,
    showOnlyBookmarked: Boolean,
    toggleShowOnlyBookmarked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            onClick = toggleShowOnlyNotChecked,
            label = { Text(text = stringResource(R.string.unchecked)) },
            selected = showOnlyNotChecked
        )
        FilterChip(
            onClick = toggleShowOnlyBookmarked,
            label = { Text(text = stringResource(R.string.bookmarked)) },
            selected = showOnlyBookmarked
        )
    }
}


@Composable
private fun SubtopicsLazyColumn(
    filteredSubtopics: List<Subtopic>,
    navigateToSubtopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = rememberLazyListState()
    // Hide keyboard when scrolling
    if (state.isScrollInProgress) {
        keyboardController?.hide()
    }

    LazyColumn(modifier = modifier, state = state) {
        items(filteredSubtopics) { subtopic ->
            SubtopicListItem(
                subtopic = subtopic,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navigateToSubtopic(subtopic.id) }
            )
        }
    }
}

@Composable
private fun SubtopicListItem(subtopic: Subtopic, modifier: Modifier) {
    ListItem(
        headlineContent = { Text(subtopic.title, overflow = Ellipsis, maxLines = 1) },
        modifier = modifier,
        leadingContent = {
            Icon(
                painter = painterResource(
                    id = if (subtopic.bookmarked) {
                        R.drawable.baseline_bookmark_24
                    } else {
                        R.drawable.baseline_bookmark_border_24
                    }
                ),
                modifier = Modifier,
                contentDescription = stringResource(
                    id = if (subtopic.bookmarked) {
                        R.string.bookmarked
                    } else {
                        R.string.bookmarked
                    }
                )
            )
        },
        trailingContent = {
            Checkbox(
                checked = subtopic.checked,
                enabled = false,
                onCheckedChange = null,
            )
        },
    )
}

@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun SubtopicsScreenPreview() {
    StudyAppTheme {
        SubtopicsScreen(
            uiState = SubtopicsUiState.Success(
                subtopics = listOf(
                    Subtopic(
                        id = 1,
                        topicId = 0,
                        title = "Golden Retriever",
                        description = "Friendly, intelligent, and great with families.",
                        checked = false,
                        bookmarked = false,
                        imageUri = null
                    ),
                    Subtopic(
                        id = 2,
                        topicId = 0,
                        title = "Labrador Retriever",
                        description = "Outgoing, loyal, and super trainable.",
                        checked = false,
                        bookmarked = false,
                        imageUri = null
                    ),
                    Subtopic(
                        id = 3,
                        topicId = 0,
                        title = "German Shepherd",
                        description = "Brave, confident, and excellent working dogs.",
                        checked = false,
                        bookmarked = false,
                        imageUri = null
                    ),
                    Subtopic(
                        id = 4,
                        topicId = 0,
                        title = "Pomeranian",
                        description = "Small, fluffy, and full of personality.",
                        checked = false,
                        bookmarked = false,
                        imageUri = null
                    ),
                    Subtopic(
                        id = 5,
                        topicId = 0,
                        title = "Border Collie",
                        description = "Highly energetic and the smartest of all breeds.",
                        checked = false,
                        bookmarked = false,
                        imageUri = null
                    ),
                    Subtopic(
                        id = 6,
                        topicId = 0,
                        title = "Dachshund",
                        description = "Long-bodied and playful with a bold attitude.",
                        checked = false,
                        bookmarked = false,
                        imageUri = null
                    ),
                    Subtopic(
                        id = 7,
                        topicId = 0,
                        title = "French Bulldog",
                        description = "Compact and charming with a lovable snort.",
                        checked = false,
                        bookmarked = false,
                        imageUri = null
                    ),
                    Subtopic(
                        id = 8,
                        topicId = 0,
                        title = "Cocker Spaniel",
                        description = "Gentle, sweet, and always ready to cuddle.",
                        checked = false,
                        bookmarked = false,
                        imageUri = null
                    ),
                    Subtopic(
                        id = 9,
                        topicId = 0,
                        title = "Great Dane",
                        description = "A gentle giant with a calm and loving nature.",
                        checked = false,
                        bookmarked = false,
                        imageUri = null
                    ),
                    Subtopic(
                        id = 10,
                        topicId = 0,
                        title = "Siberian Husky",
                        description = "Beautiful, energetic, and known for their striking blue eyes.",
                        checked = false,
                        bookmarked = false,
                        imageUri = null
                    )
                ),
                topicsWithProgress = listOf(
                    TopicWithProgress(
                        topic = Topic(id = 0, title = "Topic 0"),
                        checked = true
                    )
                ),
                selectedTopic = Topic(id = 0, title = "Android Taint Analysis")
            ),
            navigateToSubtopic = {},
            navigateToTopic = {},
            navigateBack = {},
            saveSubtopic = {},
            deleteTopic = {},
            updateTopic = {}
        )
    }
}

@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun SubtopicsScreenLoadingPreview() {
    StudyAppTheme {
        SubtopicsScreen(
            uiState = SubtopicsUiState.Loading,
            navigateToSubtopic = {},
            navigateToTopic = {},
            navigateBack = {},
            saveSubtopic = {},
            deleteTopic = {},
            updateTopic = {},
        )
    }
}

