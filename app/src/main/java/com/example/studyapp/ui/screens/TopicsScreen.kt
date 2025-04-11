package com.example.studyapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Create
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
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.TopicsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsScreen(topicsViewModel: TopicsViewModel, modifier: Modifier = Modifier) {
    TopicsScaffold(
        topics = topicsViewModel.topics.collectAsState().value,
        createTopic = { title ->
            topicsViewModel.createTopic(title)
        },
        modifier = modifier
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicsScaffold(
    topics: List<Topic>,
    createTopic: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(R.string.topics_search),
                        modifier = Modifier.size(24.dp)
                    )
                },
                actions = { MoreActionsMenu() },
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
            )
        },
        floatingActionButton = {
            CreateTopicFAB(onCreate = createTopic)
        }
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            items(topics.size) { index ->
                TopicListItem(
                    topic = topics[index],
                    modifier = Modifier
                        .clickable { /* TODO Implement */ }
                )
            }
        }
    }
}

@Composable
private fun TopicListItem(topic: Topic, modifier: Modifier) {
    ListItem(
        headlineContent = { Text(topic.title) },
        modifier = modifier,
        trailingContent = {
            Checkbox(
                checked = topic.checked,
                onCheckedChange = { /* TODO Implement */ },
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
private fun CreateTopicFAB(onCreate: (String) -> Unit, modifier: Modifier = Modifier) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    FloatingActionButton(onClick = { showDialog = true }, modifier = modifier) {
        Icon(
            imageVector = Icons.Outlined.Create,
            contentDescription = stringResource(R.string.create)
        )
    }
    if (showDialog) {
        // ImagePicker()
        CreateTopicDialog(
            onDismiss = { showDialog = false },
            onCreate = onCreate
        )
    }
}

@Composable
private fun CreateTopicDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { },
    onCreate: (String) -> Unit = { }
) {
    var topicTitle by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.create_topic)) },
        text = {
            TextField(
                value = topicTitle,
                onValueChange = { topicTitle = it }, // Update the state with new value
                label = { Text(stringResource(R.string.title)) }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onCreate(topicTitle)
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
        TopicsScaffold(
            topics = listOf(
                Topic(1, "Topic 1", false),
                Topic(2, "Topic 2", true),
                Topic(3, "Topic 3", false)
            ),
            createTopic = {}
        )
    }
}
