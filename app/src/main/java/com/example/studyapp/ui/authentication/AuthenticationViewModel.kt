package com.example.studyapp.ui.authentication

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.R
import com.example.studyapp.ui.authentication.AuthenticationAlternative.GOOGLE
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor() : ViewModel() {
    private companion object {
        const val WEB_CLIENT_ID =
            "196684472942-5vjhqshte8vmb5loes1lc23t6qn8a85v.apps.googleusercontent.com"
        const val TAG = "AuthentificationViewModel"
    }

    // TODO: Implement uiState. This is just a placeholder.
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<AuthenticationUiState> = flowOf(AuthenticationUiState.Success()).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AuthenticationUiState.Loading
    )

    @VisibleForTesting
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun initiateAuthentication(
        authenticationAlternative: AuthenticationAlternative,
        context: Context
    ) {
        when (authenticationAlternative) {
            GOOGLE -> createSignInWithGoogleFlow(context = context)
            else -> {}
        }
    }

    @VisibleForTesting
    fun createSignInWithGoogleFlow(context: Context) {
        val credentialManager = CredentialManager.create(context = context)
        val getSignInWithGoogleOption = getSignInWithGoogleOption()
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(getSignInWithGoogleOption)
            .build()

        viewModelScope.launch {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            handleSignIn(result = result)
        }

    }

    @VisibleForTesting
    fun getSignInWithGoogleOption(): GetSignInWithGoogleOption {
        return GetSignInWithGoogleOption.Builder(
            serverClientId = WEB_CLIENT_ID
        )
            // TODO: Set a nonce to improve security
            //       https://developer.android.com/identity/sign-in/credential-manager-siwg#set-nonce
            //.setNonce(<nonce string to use when generating a Google ID token>)
            .build()

    }

    @VisibleForTesting
    fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential

        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        GoogleIdTokenCredential
                            .createFrom(credential.data)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

}

sealed interface AuthenticationUiState {
    object Loading : AuthenticationUiState
    data class Success(
        val currentAuthenticationAlternative: AuthenticationAlternative? = null,
        val userHasAccount: Boolean = false,
        val email: String = "",
        val password: String = ""
    ) : AuthenticationUiState

    object Error : AuthenticationUiState
}

/**
 * Represents the alternative authentication methods that can be used.
 * The standard authentication method is email and password.
 *
 * @property contentDescriptionResId The resource ID of the content description string for the button.
 * @property lightIconResId The resource ID of the drawable for the button icon in case of light theme.
 * @property darkIconResId The resource ID of the drawable for the button icon in case of dark theme.
 */
enum class AuthenticationAlternative(
    val contentDescriptionResId: Int,
    val lightIconResId: Int,
    val darkIconResId: Int
) {
    GOOGLE(
        contentDescriptionResId = R.string.authenticate_with_google,
        lightIconResId = R.drawable.google_logo_light,
        darkIconResId = R.drawable.google_logo_dark
    ),
    MICROSOFT(
        contentDescriptionResId = R.string.authenticate_with_microsoft,
        lightIconResId = R.drawable.google_logo_light, // TODO: Remove and add light icon for each authentication type.
        darkIconResId = R.drawable.google_logo_dark // TODO: Remove and add dark icon for each authentication type.
    ),
    APPLE(
        contentDescriptionResId = R.string.authenticate_with_apple,
        lightIconResId = R.drawable.google_logo_light, // TODO: Remove and add light icon for each authentication type.
        darkIconResId = R.drawable.google_logo_dark // TODO: Remove and add dark icon for each authentication type.
    ),
    BIOMETRICS(
        contentDescriptionResId = R.string.authenticate_with_biometrics,
        lightIconResId = R.drawable.google_logo_light, // TODO: Remove and add light icon for each authentication type.
        darkIconResId = R.drawable.google_logo_dark // TODO: Remove and add dark icon for each authentication type.
    ),
    X(
        contentDescriptionResId = R.string.authenticate_with_x,
        lightIconResId = R.drawable.google_logo_light, // TODO: Remove and add light icon for each authentication type.
        darkIconResId = R.drawable.google_logo_dark // TODO: Remove and add dark icon for each authentication type.
    ),
    FACEBOOK(
        contentDescriptionResId = R.string.authenticate_with_facebook,
        lightIconResId = R.drawable.google_logo_light, // TODO: Remove and add light icon for each authentication type.
        darkIconResId = R.drawable.google_logo_dark // TODO: Remove and add dark icon for each authentication type.
    ),
    PHONE_NUMBER(
        contentDescriptionResId = R.string.authenticate_with_phone_number,
        lightIconResId = R.drawable.google_logo_light, // TODO: Remove and add light icon for each authentication type.
        darkIconResId = R.drawable.google_logo_dark // TODO: Remove and add dark icon for each authentication type.

    )
}
