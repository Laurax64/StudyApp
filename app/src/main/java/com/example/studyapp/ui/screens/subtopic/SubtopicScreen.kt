package com.example.studyapp.ui.screens.subtopic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.viewmodels.SubtopicViewModel

@Composable
fun SubtopicScreen(
    subtopicViewModel: SubtopicViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subtopic by subtopicViewModel.subtopic.collectAsStateWithLifecycle(null)
    SubtopicScreen(
        subtopic = subtopic,
        updateSubtopic = subtopicViewModel::updateSubtopic,
        deleteSubtopic = subtopicViewModel::deleteSubtopic,
        modifier = modifier.padding(horizontal = 16.dp),
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SubtopicScreen(
    subtopic: Subtopic?,
    updateSubtopic: (Subtopic) -> Unit,
    deleteSubtopic: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (subtopic == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingIndicator()
        }
    } else {
        SubtopicScaffold(
            subtopic = subtopic,
            updateSubtopic = updateSubtopic,
            deleteSubtopic = deleteSubtopic,
            navigateBack = navigateBack,
            modifier = modifier,
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun SubtopicScreenPreview() {
    SubtopicScreen(
        subtopic = Subtopic(
            id = 1,
            title = "Subtopic Title",
            description = "Subtopic Description",
            checked = false,
            bookmarked = false,
            topicId = 1,
            imageUri = null
        ),
        updateSubtopic = {}, deleteSubtopic = {}, navigateBack = {}
    )
}