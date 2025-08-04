package com.example.studyapp.ui.authentication

import android.content.Context
import android.net.Uri
import androidx.annotation.VisibleForTesting
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.R
import com.example.studyapp.ui.authentication.AuthenticationAlternative.GOOGLE
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor() : ViewModel() {
    private companion object {
        const val WEB_CLIENT_ID =
            "196684472942-5vjhqshte8vmb5loes1lc23t6qn8a85v.apps.googleusercontent.com"
    }

    // TODO: Implement uiState. This is just a placeholder.
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: MutableStateFlow<AuthenticationUiState> =
        MutableStateFlow(AuthenticationUiState.Success())

    /**
     * Initiate the authentication flow.
     *
     * @return The first letter of the user name
     */
    internal fun initiateAuthentication(
        authenticationAlternative: AuthenticationAlternative,
        context: Context
    ) {
        return when (authenticationAlternative) {
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
        val credential = result.credential
        when (credential) {
            is PublicKeyCredential -> {
                credential.authenticationResponseJson
                // Share responseJson i.e. a GetCredentialResponse on your server to
                // validate and  authenticate
            }

            is PasswordCredential -> {
                val id = credential.id
                val password = credential.password
                // Use id and password to send to your server to validate
                // and authenticate
                uiState.update {
                    AuthenticationUiState.Success(
                        userIsSignedIn = true,
                        email = id,
                        password = password,
                    )
                }
            }

            is CustomCredential -> {
                when (credential.type) {
                    GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                        val result = GoogleIdTokenCredential.createFrom(credential.data)
                        uiState.update {
                            AuthenticationUiState.Success(
                                userIsSignedIn = true,
                                email = result.id,
                                currentAuthenticationAlternative = GOOGLE,
                                profilePictureUri = result.profilePictureUri
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed interface AuthenticationUiState {
    object Loading : AuthenticationUiState

    data class Success(
        val currentAuthenticationAlternative: AuthenticationAlternative? = null,
        val userHasAccount: Boolean = false,
        val userIsSignedIn: Boolean = false,
        val phoneNumber: String? = null,
        val userId: String? = null,
        val email: String? = null,
        val password: String? = null,
        val profilePictureUri: Uri? = null
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
        lightIconResId = R.drawable.microsoft_logo,
        darkIconResId = R.drawable.microsoft_logo
    ),
    APPLE(
        contentDescriptionResId = R.string.authenticate_with_apple,
        lightIconResId = R.drawable.apple_logo_dark,
        darkIconResId = R.drawable.apple_logo_light
    ),
    BIOMETRICS(
        contentDescriptionResId = R.string.authenticate_with_biometrics,
        lightIconResId = R.drawable.fingerprint_dark,
        darkIconResId = R.drawable.fingerprint_light
    ),
    X(
        contentDescriptionResId = R.string.authenticate_with_x,
        lightIconResId = R.drawable.x_logo_dark,
        darkIconResId = R.drawable.x_logo_light
    ),
    FACEBOOK(
        contentDescriptionResId = R.string.authenticate_with_facebook,
        lightIconResId = R.drawable.facebook_logo,
        darkIconResId = R.drawable.facebook_logo
    ),
    PHONE_NUMBER(
        contentDescriptionResId = R.string.authenticate_with_phone_number,
        lightIconResId = R.drawable.smartphone_dark,
        darkIconResId = R.drawable.smartphone_light
    )
}
