package com.example.studyapp.ui.screens.subtopics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.SubtopicsViewModel

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
    if (topic == null || subtopics == null || topics == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        SubtopicsScaffold(
            subtopics = subtopics,
            topics = topics,
            topic = topic,
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

