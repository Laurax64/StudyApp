package com.example.studyapp.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.Topic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenDialog(
    titleRes: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmButtonStringRes: Int = R.string.save,
    dismissIconRes: Int = R.drawable.baseline_close_24,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = titleRes),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = dismissIconRes),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(R.string.cancel),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDismiss() }
                    )
                },
                actions = {
                    TextButton(onClick = onConfirm) { Text(stringResource(id = confirmButtonStringRes)) }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = modifier,
    ) {
        content(it)
    }
}

@Composable
fun TopicDialog(
    titleRes: Int,
    topic: Topic?,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSave: (Topic) -> Unit
) {
    var topicTitle by rememberSaveable { mutableStateOf(topic?.title ?: "") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(titleRes)) },
        text = {
            OutlinedTextField(
                value = topicTitle,
                onValueChange = { topicTitle = it }, // Update the state with new value
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
fun SubtopicFullScreenDialog(
    titleRes: Int,
    onDismiss: () -> Unit,
    saveSubtopic: (String, String, String?) -> Unit,
    modifier: Modifier = Modifier,
    subtopic: Subtopic? = null,
) {
    var title by rememberSaveable { mutableStateOf(subtopic?.title ?: "") }
    var description by rememberSaveable { mutableStateOf(subtopic?.description ?: "") }
    var imageUri by rememberSaveable { mutableStateOf(subtopic?.imageUri ?: "") }
    FullScreenDialog(
        titleRes = titleRes,
        onDismiss = onDismiss,
        onConfirm = { saveSubtopic(title, description, imageUri) },
        modifier = modifier.padding(horizontal = 16.dp)
    ) { innerPadding ->
        SubtopicInputFields(
            updateTitle = { title = it },
            updateDescription = { description = it },
            updateImageUri = { imageUri = it },
            title = title,
            description = description,
            imageUri = imageUri,
            modifier = Modifier.padding(paddingValues = innerPadding),
        )
    }
}

@Composable
private fun SubtopicInputFields(
    updateTitle: (String) -> Unit,
    updateDescription: (String) -> Unit,
    updateImageUri: (String) -> Unit,
    title: String,
    description: String,
    imageUri: String,
    modifier: Modifier = Modifier,
) {
    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        updateImageUri(uri.toString())
    }
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // TODO remove focus when user clicks outside of TextField
        OutlinedTextField(
            value = title,
            onValueChange = { updateTitle(it) },
            modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.title)) })
        OutlinedTextField(
            value = description,
            onValueChange = { updateDescription(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.description)) })
        OutlinedTextField(
            value = imageUri,
            onValueChange = { updateImageUri(it) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.outline_image_24),
                    contentDescription = stringResource(R.string.image),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                        }
                )
            },
            label = { Text(stringResource(R.string.image)) })
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/* TODO add missing fields */
@Composable
private fun CreateSubtopicDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { },
    onCreate: (String, String, String?) -> Unit
) {
    var subtopicTitle by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.create_subtopic)) },
        text = {
            TextField(
                value = subtopicTitle,
                onValueChange = { subtopicTitle = it }, // Update the state with new value
                label = { Text(stringResource(R.string.title)) })
        },
        confirmButton = {
            TextButton(
                onClick = {
                    //   onCreate(title = subtopicTitle, description = ) TODO
                    onDismiss()
                }) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        },
        modifier = modifier
    )
}

@Composable
@Preview(showSystemUi = true)
private fun SubtopicFullScreenDialogPreview() {
    SubtopicFullScreenDialog(
        titleRes = R.string.create_subtopic,
        onDismiss = { },
        saveSubtopic = { _, _, _ -> },
        subtopic = null
    )
}