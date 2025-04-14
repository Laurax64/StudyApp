package com.example.studyapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenDialog(
    titleRes: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmButtonStringRes: Int = R.string.save,
    dismissIconRes: Int? = R.drawable.baseline_close_24,
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = titleRes)) },
                navigationIcon = {
                    TopBarLeadingIcon(
                        iconRes = R.drawable.baseline_close_24,
                        onClick = onDismiss
                    )
                },
                modifier = Modifier
                    .size(56.dp)
                    .padding(horizontal = 24.dp),
                actions = {
                    TextButton(onClick = onConfirm) { Text(stringResource(id = confirmButtonStringRes)) }
                }
            )
        },
        modifier = modifier,
    ) {
        content(Modifier.padding(it))
    }
}