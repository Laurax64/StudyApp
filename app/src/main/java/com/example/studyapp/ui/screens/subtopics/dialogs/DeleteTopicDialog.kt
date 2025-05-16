package com.example.studyapp.ui.screens.subtopics.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.studyapp.R
import com.example.studyapp.ui.theme.StudyAppTheme

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
        text = { Text(stringResource(R.string.delete_topic_dialog_description, topicTitle)) },
        confirmButton = { TextButton(onClick = deleteTopic) { Text(stringResource(R.string.delete)) } },
        dismissButton = { TextButton(onClick = closeDialog) { Text(stringResource(R.string.cancel)) } },
        modifier = modifier
    )
}


@PreviewLightDark
@Composable
fun DeleteTopicDialogPreview() {
    StudyAppTheme {
        DeleteTopicDialog(topicTitle = "Topic", deleteTopic = {}, closeDialog = {})
    }
}