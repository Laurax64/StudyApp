package com.example.studyapp.ui.authentication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import com.example.studyapp.R
import com.example.studyapp.ui.components.FullScreenDialog
import com.example.studyapp.ui.theme.StudyAppTheme

data class AuthenticationAlternative(
    val type: AuthenticationAlternativeType,
    val contentDescriptionResId: Int,
    val iconResId: Int,
    val initiateAuthentication: () -> Unit
)

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
internal fun AuthenticationDialog(
    viewModel: AuthenticationViewModel,
    navigateBack: () -> Unit,
    isSignedIn: Boolean
) {
    val context = LocalContext.current
    AuthenticationDialog(
        onConfirm = {
            viewModel.initiateAuthentication(
                AuthenticationAlternativeType.GOOGLE,
                context
            )
        },
        navigateBack = navigateBack,
        isSignedIn = isSignedIn,
        initiateAuthentication = {
            viewModel.initiateAuthentication(
                context = context,
                authenticationAlternativeType = it
            )
        }
    )
}


@Composable
private fun AuthenticationDialog(
    onConfirm: () -> Unit,
    navigateBack: () -> Unit,
    isSignedIn: Boolean,
    initiateAuthentication: (AuthenticationAlternativeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val inputFields =
        @Composable { innerPadding: PaddingValues ->
            AuthentificationInputFields(
                initialEmail = "",
                initialPassword = "",
                initiateAuthentication = initiateAuthentication,
                modifier = Modifier.padding(innerPadding)
            )
        }

    val isScreenWidthCompact = !currentWindowAdaptiveInfo().windowSizeClass
        .isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)

    if (isScreenWidthCompact) {
        FullScreenDialog(
            titleId = if (isSignedIn) R.string.sign_into_your_account else R.string.create_a_new_account,
            onDismiss = navigateBack,
            onConfirm = onConfirm,
            modifier = modifier,
            confirmButtonStringRes = if (isSignedIn) R.string.sign_in else R.string.sign_up,
            dismissIconRes = R.drawable.baseline_close_24
        ) { innerPadding ->
            inputFields(innerPadding)
        }
    } else {
        // TODO: Implement alert dialog for medium and expanded screen width.
    }
}

@Composable
private fun AuthentificationInputFields(
    initialEmail: String,
    initialPassword: String,
    initiateAuthentication: (AuthenticationAlternativeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf(value = initialEmail) }
    var password by rememberSaveable { mutableStateOf(value = initialPassword) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    if (scrollState.isScrollInProgress) {
        keyboardController?.hide()
    }
    val authenticationOptions = AuthenticationAlternativeType.entries.map {
        AuthenticationAlternative(
            contentDescriptionResId = it.contentDescriptionResId,
            iconResId = if (isSystemInDarkTheme()) it.darkIconResId else it.lightIconResId,
            initiateAuthentication = { initiateAuthentication(it) },
            type = it
        )
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
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.password)) }
        )
        AuthentificationOptionsButtonGroup(
            authenticationAlternatives = authenticationOptions,
        )

    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AuthentificationOptionsButtonGroup(
    modifier: Modifier = Modifier,
    authenticationAlternatives: List<AuthenticationAlternative>,
) {
    ButtonGroup(
        modifier = modifier,
        overflowIndicator = {/*TODO*/ }
    ) {
        authenticationAlternatives.forEach { option ->
            clickableItem(
                onClick = option.initiateAuthentication,
                label = "",
                icon = {
                    Image(
                        painter = painterResource(id = option.iconResId),
                        contentDescription = "Localized description",
                    )
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
            isSignedIn = false,
            initiateAuthentication = {}
        )
    }
}