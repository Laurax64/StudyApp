package com.example.studyapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties

@Composable
fun AlertDialog(
    titleResId: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmButtonTextResId: Int,
    dismissButtonTextResId: Int,
    properties: DialogProperties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = false
    ),
    content: @Composable (Modifier) -> Unit,
) {
    AlertDialog(
        title = stringResource(titleResId),
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        modifier = modifier,
        properties = properties,
        confirmButtonText = stringResource(id = confirmButtonTextResId),
        dismissButtonText = stringResource(id = dismissButtonTextResId),
        content = content,
    )
}


@Composable
fun AlertDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmButtonText: String,
    dismissButtonText: String,
    properties: DialogProperties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = false
    ),
    content: @Composable (Modifier) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        properties = properties,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissButtonText)
            }
        },
        text = {
            content(Modifier.fillMaxWidth())
        },
        modifier = modifier
    )
}