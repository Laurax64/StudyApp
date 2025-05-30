package com.example.studyapp.ui.screens.subtopic

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.components.study.SaveSubtopicDialog

enum class DialogType {
    EDIT_SUBTOPIC,
    DELETE_SUBTOPIC
}

@Composable
internal fun SubtopicDialog(
    subtopic: Subtopic,
    isScreenWidthCompact: Boolean,
    deleteSubtopic: () -> Unit,
    dismissDialog: () -> Unit,
    dialogType: DialogType,
    updateSubtopic: (Subtopic) -> Unit,
    modifier: Modifier = Modifier
) {
    when (dialogType) {
        DialogType.DELETE_SUBTOPIC ->
            DeleteSubtopicDialog(
                modifier = modifier,
                onDismiss = dismissDialog,
                deleteSubtopic = deleteSubtopic,
                subtopicTitle = subtopic.title
            )

        DialogType.EDIT_SUBTOPIC ->
            SaveSubtopicDialog(
                modifier = modifier,
                titleId = R.string.edit_subtopic,
                onDismiss = dismissDialog,
                isFullScreenDialog = isScreenWidthCompact,
                saveSubtopic = { title, description, imageUri ->
                    updateSubtopic(
                        subtopic.copy(
                            title = title,
                            description = description,
                            imageUri = imageUri
                        )
                    )
                },
                subtopic = subtopic
            )
    }
}

@Composable
private fun DeleteSubtopicDialog(
    modifier: Modifier = Modifier,
    subtopicTitle: String,
    deleteSubtopic: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_subtopic_dialog_title)) },
        text = { Text(stringResource(R.string.delete_subtopic_dialog_description, subtopicTitle)) },
        confirmButton = {
            TextButton(onClick = {
                deleteSubtopic()
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
