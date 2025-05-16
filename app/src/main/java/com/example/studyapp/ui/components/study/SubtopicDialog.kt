package com.example.studyapp.ui.components.study

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.components.FullScreenDialog

@Composable
fun SubtopicDialog(
    titleId: Int,
    onDismiss: () -> Unit,
    saveSubtopic: (String, String, String?) -> Unit,
    modifier: Modifier = Modifier,
    isWidthAtLeastMedium: Boolean,
    subtopic: Subtopic? = null,
) {
    var title by rememberSaveable { mutableStateOf(subtopic?.title ?: "") }
    var description by rememberSaveable { mutableStateOf(subtopic?.description ?: "") }
    var imageUri by rememberSaveable { mutableStateOf(subtopic?.imageUri ?: "") }

    if (isWidthAtLeastMedium) {
        AlertDialog(
            // Dialog should not close when clicking outside.
            onDismissRequest = {},
            title = { Text(stringResource(titleId)) },
            text = {
                SubtopicInputFields(
                    updateTitle = { title = it },
                    updateDescription = { description = it },
                    updateImageUri = { imageUri = it },
                    title = title,
                    description = description,
                    imageUri = imageUri,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        saveSubtopic(title, description, imageUri)
                        onDismiss()
                    }
                ) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            modifier = modifier
        )
    } else {
        FullScreenDialog(
            titleRes = titleId,
            onDismiss = onDismiss,
            onConfirm = {
                saveSubtopic(title, description, imageUri)
                onDismiss()
            },
            modifier = modifier.padding(horizontal = 16.dp)
        ) { innerPadding ->
            SubtopicInputFields(
                updateTitle = { title = it },
                updateDescription = { description = it },
                updateImageUri = { imageUri = it },
                title = title,
                description = description,
                imageUri = imageUri,
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
            )
        }
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
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    if (scrollState.isScrollInProgress) {
        keyboardController?.hide()
    }
    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        updateImageUri(uri.toString())
    }
    Column(
        modifier = modifier
            .verticalScroll(state = scrollState)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { updateTitle(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.title)) },
        )

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
        AsyncImage(model = imageUri, contentDescription = null, modifier = Modifier.fillMaxWidth())
    }
}
