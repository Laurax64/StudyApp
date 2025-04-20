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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic

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
                }
            )
        },
        modifier = modifier,
    ) {
        content(it)
    }
}

@Composable
fun SubtopicFullScreenDialog(
    titleRes: Int,
    onDismiss: () -> Unit,
    saveSubtopic: (String, String, String?) -> Unit,
    modifier: Modifier = Modifier,
    subtopic: Subtopic? = null,
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var imageUri: String? by rememberSaveable { mutableStateOf(null) }
    var saveData by rememberSaveable { mutableStateOf(false) }

    if (saveData) {
        saveSubtopic(title, description, imageUri)
    }
    FullScreenDialog(
        titleRes = titleRes,
        onDismiss = onDismiss,
        onConfirm = { saveData = true },
        modifier = modifier.padding(horizontal = 24.dp)
    ) { innerPadding ->
        SubtopicInputFields(
            updateTitle = { title = it },
            updateDescription = { description = it },
            updateImageUri = { imageUri = it },
            originalTitle = subtopic?.title ?: "",
            originalDescription = subtopic?.description ?: "",
            originalImageUri = subtopic?.imageUri ?: "",
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun SubtopicInputFields(
    updateTitle: (String) -> Unit,
    updateDescription: (String) -> Unit,
    updateImageUri: (String) -> Unit,
    originalTitle: String,
    originalDescription: String,
    originalImageUri: String,
    modifier: Modifier = Modifier,
) {
    var title by rememberSaveable { mutableStateOf(originalTitle) }
    var description by rememberSaveable { mutableStateOf(originalDescription) }
    var imageUri by rememberSaveable { mutableStateOf(originalImageUri) }
    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        imageUri = uri.toString()
    }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextField(value = title, onValueChange = {
            title = it
            updateTitle(title)
        }, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.title)) })
        TextField(
            value = description,
            onValueChange = {
                description = it
                updateDescription(description)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.description)) })
        TextField(value = imageUri, onValueChange = {
            imageUri = it
            updateImageUri(imageUri)
        }, modifier = Modifier.fillMaxWidth(), trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.outline_image_24),
                contentDescription = stringResource(R.string.image),
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    }
            )
        }, label = { Text(stringResource(R.string.image)) })
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
                Text(stringResource(R.string.create))
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
