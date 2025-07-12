package com.example.studyapp.ui.study.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.data.study.Topic
import com.example.studyapp.ui.components.AlertDialog

@Composable
internal fun SaveTopicDialog(
    topic: Topic?,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSave: (Topic) -> Unit
) {
    var topicTitle by rememberSaveable { mutableStateOf(topic?.title ?: "") }

    AlertDialog(
        titleResId = if (topic == null) {
            R.string.create_topic
        } else {
            R.string.edit_topic
        },
        onDismiss = onDismiss,
        onConfirm = {
            if (topic != null) {
                onSave(topic.copy(title = topicTitle))
            } else {
                onSave(Topic(title = topicTitle))
            }
        },
        confirmButtonTextResId = R.string.save,
        dismissButtonTextResId = R.string.cancel,
        modifier = modifier,
        content = {
            TopicInputField(
                updateTitle = { topicTitle = it },
                title = topicTitle,
            )
        }
    )
}


@Composable
private fun TopicInputField(
    updateTitle: (String) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    if (scrollState.isScrollInProgress) {
        keyboardController?.hide()
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
    }
}
