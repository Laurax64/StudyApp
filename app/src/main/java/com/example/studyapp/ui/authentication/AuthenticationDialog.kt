package com.example.studyapp.ui.authentication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import com.example.studyapp.R
import com.example.studyapp.ui.components.FullScreenDialog
import com.example.studyapp.ui.theme.StudyAppTheme


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AuthenticationDialog(
    viewModel: AuthenticationViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    AuthenticationDialog(
        onConfirm = {
            viewModel.initiateAuthentication(
                AuthenticationAlternative.GOOGLE, context
            )
        },
        navigateBack = navigateBack,
        isSignedIn = true,
        initiateAuthentication = {
            viewModel.initiateAuthentication(
                context = context, authenticationAlternative = it
            )
        }
    )
}


@Composable
private fun AuthenticationDialog(
    onConfirm: () -> Unit,
    navigateBack: () -> Unit,
    isSignedIn: Boolean,
    initiateAuthentication: (AuthenticationAlternative) -> Unit,
    modifier: Modifier = Modifier,
) {
    val inputFields = @Composable { dialogContentModifier: Modifier ->
        AuthentificationInputColumn(
            initiateAuthentication = initiateAuthentication,
            uiState = AuthenticationUiState.Success(),
            modifier = dialogContentModifier
        )
    }

    val isScreenWidthCompact =
        !currentWindowAdaptiveInfo().windowSizeClass.isWidthAtLeastBreakpoint(
            WIDTH_DP_EXPANDED_LOWER_BOUND
        )

    if (isScreenWidthCompact) {
        FullScreenDialog(
            titleId = if (isSignedIn) R.string.sign_into_your_account else R.string.create_a_new_account,
            onDismiss = navigateBack,
            onConfirm = onConfirm,
            modifier = modifier,
            confirmButtonStringRes = if (isSignedIn) R.string.sign_in else R.string.sign_up,
            dismissIconRes = R.drawable.baseline_close_24,
            content = inputFields
        )
    } else {
        // TODO: Implement alert dialog for medium and expanded screen width.
    }
}

@Composable
private fun AuthentificationInputColumn(
    uiState: AuthenticationUiState,
    initiateAuthentication: (AuthenticationAlternative) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState is AuthenticationUiState.Success) {
        var email by rememberSaveable { mutableStateOf(value = uiState.email) }
        var password by rememberSaveable { mutableStateOf(value = uiState.password) }
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val scrollState = rememberScrollState()
        if (scrollState.isScrollInProgress) {
            keyboardController?.hide()
        }
        AuthenticationAlternative.entries
        Column(
            modifier = modifier
                .verticalScroll(state = scrollState)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.email)) },
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.password)) })
            AuthentificationOptionsButtonGroup(
                initiateAuthentication = initiateAuthentication
            )
            if (uiState.userHasAccount) {
                TextTextButtonRow(
                    textResId = R.string.already_have_an_account,
                    textButtonResId = R.string.sign_in,
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (!uiState.userHasAccount) {
                TextTextButtonRow(
                    textResId = R.string.do_not_have_an_account,
                    textButtonResId = R.string.sign_up,
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun TextTextButtonRow(
    modifier: Modifier = Modifier,
    textResId: Int,
    textButtonResId: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(textResId),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
        )
        TextButton(onClick = onClick) {
            Text(text = stringResource(textButtonResId))
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AuthentificationOptionsButtonGroup(
    modifier: Modifier = Modifier,
    initiateAuthentication: (AuthenticationAlternative) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = ButtonGroupDefaults.HorizontalArrangement,
    ) {
        AuthenticationAlternative.entries.forEach { authOption ->
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .size(IconButtonDefaults.smallContainerSize()),
                onClick = { initiateAuthentication(authOption) }
            ) {
                Image(
                    painter = painterResource(
                        id = if (isSystemInDarkTheme()) authOption.darkIconResId else authOption.lightIconResId
                    ),
                    contentDescription = stringResource(authOption.contentDescriptionResId)
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun AuthenticationDialogPreview() {
    StudyAppTheme {
        AuthenticationDialog(
            onConfirm = {},
            navigateBack = {},
            isSignedIn = false,
            initiateAuthentication = {})
    }
}