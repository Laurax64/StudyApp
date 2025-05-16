package com.example.studyapp.ui.screens.subtopics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.components.study.SubtopicDialog
import com.example.studyapp.ui.components.study.TopicDialog
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.SubtopicsViewModel

private enum class DialogType {
    EDIT_TOPIC,
    DELETE_TOPIC,
    CREATE_SUBTOPIC,
}

@Composable
fun SubtopicsScreen(
    subtopicsViewModel: SubtopicsViewModel,
    navigateToSubtopic: (Int) -> Unit,
    navigateToTopic: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topic by subtopicsViewModel.topic.collectAsStateWithLifecycle()
    val subtopics by subtopicsViewModel.subtopics.collectAsStateWithLifecycle()
    val topics by subtopicsViewModel.topics.collectAsStateWithLifecycle()
    SubtopicsScreen(
        subtopics = subtopics,
        topics = topics,
        saveSubtopic = { title, description, imageUri ->
            subtopicsViewModel.createSubtopic(
                title = title, description = description, imageUri = imageUri
            )
        },
        deleteTopic = subtopicsViewModel::deleteTopic,
        updateTopic = subtopicsViewModel::updateTopic,
        navigateToSubtopic = navigateToSubtopic,
        navigateToTopic = navigateToTopic,
        navigateBack = navigateBack,
        topic = topic ?: return,
        modifier = modifier
    )
}

@Composable
private fun SubtopicsScreen(
    subtopics: List<Subtopic>?,
    topics: List<Topic>?,
    topic: Topic?,
    saveSubtopic: (String, String, String?) -> Unit,
    deleteTopic: () -> Unit,
    updateTopic: (Topic) -> Unit,
    navigateToSubtopic: (Int) -> Unit,
    navigateToTopic: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isWidthCompact = !currentWindowAdaptiveInfo().windowSizeClass
        .isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
    var dialogType by rememberSaveable { mutableStateOf<DialogType?>(null) }
    if (topic == null || subtopics == null || topics == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        if (dialogType == DialogType.CREATE_SUBTOPIC && isWidthCompact) {
            SubtopicDialog(
                titleId = R.string.create_subtopic,
                onDismiss = { dialogType = null },
                isWidthAtLeastMedium = false,
                modifier = modifier,
                saveSubtopic = { title, description, imageUri ->
                    saveSubtopic(title, description, imageUri)
                },
            )
        } else {
            when (dialogType) {
                DialogType.EDIT_TOPIC ->
                    TopicDialog(
                        topic = topic,
                        onDismiss = { dialogType = null },
                        onSave = {
                            updateTopic(it)
                            dialogType = null
                        }
                    )

                DialogType.DELETE_TOPIC ->
                    DeleteTopicDialog(
                        onDismiss = { dialogType = null },
                        deleteTopic = {
                            // Navigate back because the topic is about to be deleted.
                            navigateBack()
                            deleteTopic()
                        },
                        topicTitle = topic.title
                    )

                DialogType.CREATE_SUBTOPIC ->
                    SubtopicDialog(
                        titleId = R.string.create_subtopic,
                        onDismiss = { dialogType = null },
                        isWidthAtLeastMedium = true,
                        saveSubtopic = { title, description, imageUri ->
                            saveSubtopic(title, description, imageUri)
                        },
                    )

                null -> {}
            }
            SubtopicsScaffold(
                subtopics = subtopics,
                topics = topics,
                topic = topic,
                onCreateSubtopic = { dialogType = DialogType.CREATE_SUBTOPIC },
                onDeleteTopic = { dialogType = DialogType.DELETE_TOPIC },
                onEditTopic = { dialogType = DialogType.EDIT_TOPIC },
                navigateToSubtopic = navigateToSubtopic,
                navigateToTopic = navigateToTopic,
                navigateBack = navigateBack,
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun DeleteTopicDialog(
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
            TextButton(onClick = {
                deleteTopic()
                onDismiss()
            }
            ) { Text(stringResource(R.string.delete)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } },
        modifier = modifier
    )
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun SubtopicsScreenPreview() {
    StudyAppTheme {
        SubtopicsScreen(
            subtopics = listOf(
                Subtopic(
                    id = 1,
                    topicId = 0,
                    title = "Subtopic 1",
                    description = "Description 1",
                    checked = false,
                    bookmarked = false,
                    imageUri = null
                ),
                Subtopic(
                    id = 2,
                    topicId = 0,
                    title = "Subtopic 2",
                    description = "Description 2",
                    checked = true,
                    bookmarked = true,
                    imageUri = null
                ),
                Subtopic(
                    id = 3,
                    topicId = 0,
                    title = "Subtopic 3",
                    description = "Description 3",
                    checked = false,
                    bookmarked = false,
                    imageUri = null
                )
            ),
            topics = listOf(Topic(id = 1, title = "Topic 1", checked = false)),
            topic = Topic(
                id = 1, title = "Android Taint Analysis", checked = false
            ),
            navigateToSubtopic = {},
            navigateToTopic = {},
            navigateBack = {},
            saveSubtopic = { _, _, _ -> },
            deleteTopic = {},
            updateTopic = {}
        )
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun LoadingScreenPreview() {
    StudyAppTheme {
        SubtopicsScreen(
            subtopics = null,
            topics = null,
            topic = null,
            navigateToSubtopic = {},
            navigateToTopic = {},
            navigateBack = {},
            saveSubtopic = { _, _, _ -> },
            deleteTopic = {},
            updateTopic = {},
        )
    }
}

