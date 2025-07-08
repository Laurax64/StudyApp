package com.example.studyapp.ui.study.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenDialog(
    titleId: Int,
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
                        text = stringResource(id = titleId),
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
    ) { innerPadding ->
        content(innerPadding)
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
            titleId = R.string.create_subtopic,
            onConfirm = {},
            onDismiss = {},
            content = {
                Text(text = "Content")
            }
        )
    }
}