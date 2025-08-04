package com.example.studyapp.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.ui.components.AdaptiveDialog
import com.example.studyapp.ui.theme.StudyAppTheme

@Composable
fun AuthenticationDialog(
    authenticationUiState: AuthenticationUiState,
    initiateAuthentication: (AuthenticationAlternative) -> Unit,
    closeDialog: () -> Unit,
) {
    if (authenticationUiState is AuthenticationUiState.Success) {
        AuthenticationDialog(
            onConfirm = {
                // TODO: Add sign in in case of email and password authentication
                closeDialog()
            },
            navigateBack = closeDialog,
            authenticationUiState = authenticationUiState,
            initiateAuthentication = initiateAuthentication,
        )
    }
}


@Composable
private fun AuthenticationDialog(
    onConfirm: () -> Unit,
    navigateBack: () -> Unit,
    authenticationUiState: AuthenticationUiState.Success,
    initiateAuthentication: (AuthenticationAlternative) -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleResId =
        if (authenticationUiState.userHasAccount) R.string.sign_into_your_account else R.string.create_a_new_account

    AdaptiveDialog(
        titleResId = titleResId,
        onDismiss = navigateBack,
        onConfirm = onConfirm,
        modifier = modifier.fillMaxSize(),
        confirmButtonTextResId = if (authenticationUiState.userHasAccount) R.string.sign_in else R.string.sign_up,
        dismissIconResId = R.drawable.baseline_close_24,
        content = { dialogContentModifier ->
            AuthentificationInputColumn(
                initiateAuthentication = initiateAuthentication,
                uiState = AuthenticationUiState.Success(),
                modifier = dialogContentModifier
            )
        }
    )
}

@Composable
private fun AuthentificationInputColumn(
    uiState: AuthenticationUiState,
    initiateAuthentication: (AuthenticationAlternative) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState is AuthenticationUiState.Success) {
        var email by rememberSaveable { mutableStateOf(value = uiState.email ?: "") }
        var password by rememberSaveable { mutableStateOf(value = uiState.password ?: "") }
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
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.email)) },
            )
            PasswordTextField(password = password, modifier = Modifier.fillMaxWidth())
            AuthentificationOptionsButtonGroup(
                initiateAuthentication = initiateAuthentication,
                currentAuthenticationAlternative = uiState.currentAuthenticationAlternative
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
private fun PasswordTextField(password: String, modifier: Modifier = Modifier) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    OutlinedSecureTextField(
        modifier = modifier,
        state = rememberTextFieldState(initialText = password),
        label = { Text(stringResource(R.string.password)) },
        textObfuscationMode =
            if (passwordHidden) TextObfuscationMode.RevealLastTyped
            else TextObfuscationMode.Visible,
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                Icon(
                    painter = painterResource(
                        if (passwordHidden) R.drawable.outline_visibility_off_24 else R.drawable.outline_visibility_24
                    ),
                    contentDescription = if (
                        passwordHidden
                    ) stringResource(
                        R.string.show_password
                    ) else stringResource(
                        R.string.hide_password
                    )
                )
            }
        },
    )
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
    currentAuthenticationAlternative: AuthenticationAlternative?,
    initiateAuthentication: (AuthenticationAlternative) -> Unit,
) {
    ButtonGroup(
        modifier = modifier,
        overflowIndicator = { menuState ->
            OutlinedIconButton(
                // TODO: Fix padding
                onClick = {
                    if (menuState.isExpanded) {
                        menuState.dismiss()
                    } else {
                        menuState.show()
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_more_vert_24),
                    contentDescription = "Localized description",
                )
            }
        }
    ) {
        AuthenticationAlternative.entries.forEach { authOption ->
            customItem(
                buttonGroupContent = {
                    OutlinedIconToggleButton(
                        checked = currentAuthenticationAlternative == authOption,
                        onCheckedChange = { initiateAuthentication(authOption) },
                        modifier = Modifier.size(IconButtonDefaults.smallContainerSize())
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (isSystemInDarkTheme()) authOption.darkIconResId else authOption.lightIconResId
                            ),
                            modifier = Modifier.size(IconButtonDefaults.smallIconSize),
                            contentDescription = stringResource(authOption.contentDescriptionResId)
                        )
                    }
                },
                menuContent = {
                    OutlinedIconButton(
                        onClick = { initiateAuthentication(authOption) },
                        modifier = Modifier.size(IconButtonDefaults.smallContainerSize())
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (isSystemInDarkTheme()) authOption.darkIconResId else authOption.lightIconResId
                            ),
                            modifier = Modifier.size(IconButtonDefaults.smallIconSize),
                            contentDescription = stringResource(authOption.contentDescriptionResId)
                        )
                    }
                }
            )
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
            authenticationUiState = AuthenticationUiState.Success(
                currentAuthenticationAlternative = AuthenticationAlternative.GOOGLE,
                email = "email",
                password = "password",
                userHasAccount = true,
            ),
            initiateAuthentication = {}
        )
    }
}