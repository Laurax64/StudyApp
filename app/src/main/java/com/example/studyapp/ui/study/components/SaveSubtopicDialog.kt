package com.example.studyapp.ui.study.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.studyapp.R
import com.example.studyapp.data.study.Subtopic
import com.example.studyapp.ui.components.FullScreenDialog
import com.example.studyapp.utils.saveToAppSpecificStorage

@Composable
internal fun SaveSubtopicDialog(
    onDismiss: () -> Unit,
    saveSubtopic: (Subtopic) -> Unit,
    modifier: Modifier = Modifier,
    isFullScreenDialog: Boolean,
    topicId: Int,
    subtopic: Subtopic? = null,
) {
    var title by rememberSaveable { mutableStateOf(subtopic?.title ?: "") }
    var description by rememberSaveable { mutableStateOf(subtopic?.description ?: "") }
    var imageUri by rememberSaveable { mutableStateOf(subtopic?.imageUri ?: "") }
    var checked by rememberSaveable { mutableStateOf(subtopic?.checked) }
    val titleId = subtopic?.let { R.string.edit_subtopic } ?: R.string.create_subtopic
    val context = LocalContext.current
    val onSave = {
        if (subtopic?.imageUri != imageUri && imageUri.isNotBlank()) {
            imageUri = saveToAppSpecificStorage(context = context, uri = imageUri.toUri())
        }
        val subtopicToSave = subtopic?.copy(
            title = title,
            description = description,
            imageUri = imageUri,
            checked = checked == true
        ) ?: Subtopic(
            title = title,
            description = description,
            imageUri = imageUri,
            checked = checked == true,
            bookmarked = false,
            topicId = topicId
        )
        saveSubtopic(subtopicToSave)
        onDismiss()
    }

    val inputFields = @Composable { innerPadding: PaddingValues ->
        SubtopicInputFields(
            updateTitle = { title = it },
            updateDescription = { description = it },
            updateImageUri = { imageUri = it },
            title = title,
            description = description,
            imageUri = imageUri,
            checked = checked,
            updateChecked = { checked = it },
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
        )
    }

    if (isFullScreenDialog) {
        FullScreenDialog(
            titleId = titleId,
            onDismiss = onDismiss,
            onConfirm = onSave,
            modifier = modifier.padding(horizontal = 16.dp)
        ) { innerPadding ->
            inputFields(innerPadding)
        }
    } else {
        AlertDialog(
            // Dialog should not close when clicking outside.
            onDismissRequest = {},
            title = { Text(stringResource(titleId)) },
            text = { inputFields(PaddingValues(0.dp)) },
            confirmButton = {
                TextButton(onClick = onSave) {
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
    }
}

@Composable
private fun SubtopicInputFields(
    updateTitle: (String) -> Unit,
    updateDescription: (String) -> Unit,
    updateImageUri: (String) -> Unit,
    updateChecked: (Boolean) -> Unit,
    title: String,
    description: String,
    imageUri: String,
    checked: Boolean?,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    if (scrollState.isScrollInProgress) {
        keyboardController?.hide()
    }
    val mediaPickerLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        updateImageUri(uri.toString())
    }

    Column(
        modifier = modifier
            .verticalScroll(state = scrollState)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }, verticalArrangement = Arrangement.spacedBy(8.dp)
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
            value = if (imageUri == "null") "" else imageUri,
            onValueChange = { updateImageUri(it) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.outline_image_24),
                    contentDescription = stringResource(R.string.image),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            mediaPickerLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                        })
            },
            label = { Text(stringResource(R.string.image)) })
        AsyncImage(model = imageUri, contentDescription = null, modifier = Modifier.fillMaxWidth())
        checked?.let {
            ToggleCheckButtonGroup(
                checked = checked,
                updateChecked = updateChecked,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ToggleCheckButtonGroup(
    checked: Boolean,
    updateChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        ToggleButton(
            checked = !checked,
            onCheckedChange = { updateChecked(false) },
            modifier = Modifier.weight(1f),
            shapes = ButtonGroupDefaults.connectedLeadingButtonShapes(),
            content = {
                Text(text = stringResource(id = R.string.i_did_not_know))
            }
        )
        ToggleButton(
            checked = checked,
            onCheckedChange = { updateChecked(true) },
            modifier = Modifier.weight(1f),
            shapes = ButtonGroupDefaults.connectedTrailingButtonShapes(),
            content = {
                Text(text = stringResource(id = R.string.i_knew_this))
            }
        )
    }
}

