package com.example.studyapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.ui.theme.StudyAppTheme

private val horizontalContentPadding = 24.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenDialog(
    titleResId: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmButtonStringRes: Int = R.string.save,
    dismissIconRes: Int = R.drawable.baseline_close_24,
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = titleResId),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onDismiss,
                    ) {
                        Icon(
                            painter = painterResource(id = dismissIconRes),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(R.string.cancel),
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onConfirm) { Text(stringResource(id = confirmButtonStringRes)) }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        content(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = horizontalContentPadding)
                .fillMaxWidth()
        )
    }
}


@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun SubtopicFullScreenDialogPreview() {
    StudyAppTheme {
        FullScreenDialog(
            titleResId = R.string.create_subtopic,
            onConfirm = {},
            onDismiss = {},
            content = { modifier ->
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = "TextField 1",
                        onValueChange = {},
                        label = { Text("Label 1") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = "TextField 2",
                        onValueChange = {},
                        label = { Text("Label 2") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        )
    }
}