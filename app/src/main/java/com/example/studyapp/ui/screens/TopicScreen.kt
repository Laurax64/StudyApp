package com.example.studyapp.ui.screens

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyapp.R
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.TopicUiState
import com.example.studyapp.ui.viewmodels.TopicViewModel

@Composable
fun TopicScreen(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onTopicClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopicViewModel = hiltViewModel(),
) {
    val topicUiState: TopicUiState by viewModel.topicUiState.collectAsStateWithLifecycle()

    TopicScreen(
        topicUiState = topicUiState,
        modifier = modifier.testTag("topic:${viewModel.topicId}"),
        showBackButton = showBackButton,
        onBackClick = onBackClick,
        onTopicClick = onTopicClick,
    )
}

@VisibleForTesting
@Composable
internal fun TopicScreen(
    topicUiState: TopicUiState,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onTopicClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
    Box(
        modifier = modifier,
    ) {
        LazyColumn(
            state = state,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            when (topicUiState) {
                TopicUiState.Loading -> item {
                    CircularProgressIndicator()
                }

                TopicUiState.Error -> TODO()
                is TopicUiState.Success -> {
                    item {
                        TopicToolbar(
                            showBackButton = showBackButton,
                            onBackClick = onBackClick,
                            uiState = topicUiState.topic,
                        )
                    }
                    topicBody(
                        name = topicUiState.topic.title,
                        onTopicClick = onTopicClick,
                    )
                }
            }
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
        topicItemsSize(topicUiState)
    }
}

private fun topicItemsSize(
    topicUiState: TopicUiState,
) = when (topicUiState) {
    TopicUiState.Error -> 0 // Nothing
    TopicUiState.Loading -> 1 // Loading bar
    is TopicUiState.Success -> 2 // Toolbar, header
}

private fun LazyListScope.topicBody(
    name: String,
    onTopicClick: (Int) -> Unit,
) {
    // TODO: Show icon if available
    item {
        TopicHeader(name)
    }

}

@Composable
private fun TopicHeader(name: String) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
    ) {
        Text(name, style = MaterialTheme.typography.displayMedium)
    }
}


@Preview
@Composable
private fun TopicBodyPreview() {
    StudyAppTheme {
        LazyColumn {
            topicBody(
                name = "Jetpack Compose",
                onTopicClick = {},
            )
        }
    }
}

@Composable
private fun TopicToolbar(
    uiState: Topic,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    ) {
        if (showBackButton) {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = stringResource(
                        id = R.string.back
                    ),
                )
            }
        }
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
fun TopicScreenPopulated() {
    StudyAppTheme {
        TopicScreen(
            topicUiState = TopicUiState.Success(
                topic = Topic(
                    id = 1,
                    title = "Jetpack Compose",
                    checked = false,
                )
            ),
            showBackButton = true,
            onBackClick = { },
            onTopicClick = TODO(),
            modifier = TODO()
        )
    }
}


@PreviewLightDark
@PreviewScreenSizes
@Composable
fun TopicScreenLoading() {
    StudyAppTheme {
        TopicScreen(
            topicUiState = TopicUiState.Loading,
            showBackButton = true,
            onBackClick = {},
            onTopicClick = {},
        )
    }
}


/**
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
deleteTopic = subtopicsViewModel::deleteTopic,
updateTopic = subtopicsViewModel::updateTopic,
navigateToSubtopic = navigateToSubtopic,
navigateBack = navigateBack,
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
deleteTopic: () -> Unit,
updateTopic: (Topic) -> Unit,
navigateToSubtopic: (Int) -> Unit,
navigateBack: () -> Unit,
modifier: Modifier = Modifier,
) {
var showDialog by rememberSaveable { mutableStateOf(false) }
if (topic == null || subtopics == null) {
Box(
modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
) { CircularProgressIndicator() }
} else {
if (showDialog) {
SubtopicFullScreenDialog(
titleRes = R.string.create_subtopic,
onDismiss = { showDialog = false },
saveSubtopic = { title, description, imageUri ->
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
Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
Icon(
painter = painterResource(R.drawable.baseline_search_24),
tint = MaterialTheme.colorScheme.onSurfaceVariant,
modifier = Modifier
.clickable {/*TODO*/ }
.size(24.dp),
contentDescription = stringResource(R.string.subtopics_search),
)
MoreActionsMenu(
deleteTopic = deleteTopic,
updateTopic = updateTopic,
topic = topic,
)
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
LazyColumn(
Modifier
.padding(paddingValues = innerPadding)
.padding(horizontal = 16.dp)
) {
items(subtopics.size) { index ->
val subtopic = subtopics[index]
SubtopicListItem(
subtopic = subtopic,
modifier = Modifier.clickable { navigateToSubtopic(subtopic.id) })
}

}
}
}
}
}

@Composable
private fun SubtopicListItem(subtopic: Subtopic, modifier: Modifier) {
ListItem(headlineContent = { Text(subtopic.title) }, modifier = modifier, trailingContent = {
Checkbox(
checked = subtopic.checked,
enabled = false,
onCheckedChange = null,
modifier = Modifier
.padding(8.dp)
.size(size = 24.dp)
)
})
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
imageVector = Icons.Default.MoreVert,
tint = MaterialTheme.colorScheme.onSurfaceVariant,
contentDescription = stringResource(R.string.menu),
modifier = Modifier.clickable { expanded = true })
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
fun EditTopicDialog(
topic: Topic?,
modifier: Modifier = Modifier,
onDismiss: () -> Unit,
onSave: (Topic) -> Unit
) {
var topicTitle by rememberSaveable { mutableStateOf(topic?.title ?: "") }

AlertDialog(
onDismissRequest = { onDismiss() },
title = { Text(stringResource(R.string.edit_topic)) },
text = {
OutlinedTextField(
value = topicTitle,
onValueChange = { topicTitle = it },
label = { Text(stringResource(R.string.title)) }
)
},
confirmButton = {
TextButton(
onClick = {
if (topic != null) {
onSave(topic.copy(title = topicTitle))
} else {
onSave(Topic(title = topicTitle, checked = false))
}
onDismiss()
}
) {
Text(stringResource(R.string.save))
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

@Composable
fun DeleteTopicDialog(
modifier: Modifier = Modifier,
topicTitle: String,
deleteTopic: () -> Unit,
closeDialog: () -> Unit,
) {
AlertDialog(
onDismissRequest = { deleteTopic() },
title = { Text(stringResource(R.string.delete_topic_dialog_title)) },
text = {
Text(
stringResource(R.string.delete_topic_dialog_description, topicTitle)
)
},
confirmButton = {
TextButton(onClick = deleteTopic) {
Text(stringResource(R.string.delete))
}
},
dismissButton = {
TextButton(onClick = closeDialog) {
Text(stringResource(R.string.cancel))
}
},
modifier = modifier
)
}


@Composable
private fun CreateSubtopicFAB(
onCreate: () -> Unit, modifier: Modifier = Modifier
) {
FloatingActionButton(onClick = onCreate, modifier = modifier) {
Icon(
painter = painterResource(R.drawable.baseline_add_24),
contentDescription = stringResource(R.string.create_subtopic),
modifier = Modifier.size(24.dp)
)
}
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun SubtopicsScreenPreview() {
StudyAppTheme {
SubtopicsScaffold(
subtopics = listOf(
Subtopic(
id = 1,
topicId = 0,
title = "Subtopic 1",
description = "Description 1",
checked = false,
imageUri = null
), Subtopic(
id = 2,
topicId = 0,
title = "Subtopic 2",
description = "Description 2",
checked = true,
imageUri = null
), Subtopic(
id = 3,
topicId = 0,
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
deleteTopic = {},
updateTopic = {}
)
}
}

@Preview
@Composable
private fun LoadingScreenPreview() {
StudyAppTheme {
SubtopicsScaffold(
subtopics = null,
topic = null,
navigateToSubtopic = {},
navigateBack = {},
createSubtopic = { _, _, _ -> },
deleteTopic = {},
updateTopic = {},
)
}
}
 */