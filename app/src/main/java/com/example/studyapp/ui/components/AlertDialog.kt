package com.example.studyapp.ui.components

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
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(titleResId)) },
        properties = properties,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(id = confirmButtonTextResId))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = dismissButtonTextResId))
            }
        },
        text = {
            content(modifier)
        },
        modifier = modifier
    )
}