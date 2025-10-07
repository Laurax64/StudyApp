package com.example.studyapp.ui.authentication

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.core.net.toUri
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialCustomException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.CreateCredentialInterruptedException
import androidx.credentials.exceptions.CreateCredentialProviderConfigurationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.publickeycredential.CreatePublicKeyCredentialDomException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.R
import com.example.studyapp.data.authentication.UserPreferences
import com.example.studyapp.data.authentication.UserPreferencesRepository
import com.example.studyapp.ui.authentication.UserIdGenerator.generateUserIdBase64
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.SecureRandom
import java.util.Base64
import java.util.Collections
import javax.inject.Inject


@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private companion object {
        const val WEB_CLIENT_ID =
            "196684472942-5vjhqshte8vmb5loes1lc23t6qn8a85v.apps.googleusercontent.com"
        const val TAG = "AuthenticationViewModel"
    }

    private val userPreferences = userPreferencesRepository.userPreferencesFlow.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
        initialValue = UserPreferences()
    )

    private val _uiState: MutableStateFlow<AuthenticationUiState> =
        MutableStateFlow(AuthenticationUiState.Loading)
    val uiState: StateFlow<AuthenticationUiState> = _uiState

    init {
        observeUserPreferences()
    }

    @VisibleForTesting
    fun observeUserPreferences() {
        viewModelScope.launch {
            userPreferences.collect { userPreferences ->
                val userId = userPreferences.userId
                _uiState.update {
                    if (userId == null) {
                        AuthenticationUiState.NotSignedIn()
                    } else {
                        val userAvatarUri = userPreferences.userAvatarUri?.toUri()
                        val currentAuthenticationAlternative =
                            userPreferences.currentAuthenticationAlternative?.let {
                                AuthenticationAlternative.valueOf(it)
                            }
                        AuthenticationUiState.SignedIn(
                            userId = userId,
                            userAvatarUri = userAvatarUri,
                            currentAuthenticationAlternative = currentAuthenticationAlternative,
                            phoneNumber = userPreferences.phoneNumber,
                            email = userPreferences.email,
                            password = userPreferences.password
                        )
                    }
                }
            }
        }
    }

    internal fun initiateAuthentication(
        authenticationAlternative: AuthenticationAlternative, context: Context
    ) {
        val credentialManager = CredentialManager.create(context = context)
        when (authenticationAlternative) {
            AuthenticationAlternative.PASSKEY -> {
                createPasskey(
                    requestJson = generateRequestJSON(),
                    credentialManager = credentialManager,
                    activityContext = context
                )
            }

            AuthenticationAlternative.GOOGLE -> createGoogleAuthenticationFlow(
                context = context, credentialManager = credentialManager
            )

            else -> GetPasswordOption()
        }
    }


    @VisibleForTesting
    fun generateRequestJSON(
        name: String = "helloandroid@gmail.com",
        email: String = "LauraVoetzsch@gmail.com"
    ): String {
        uiState.value

        // TODO: Check if challenge is correct
        val random = SecureRandom()
        val challenge = ByteArray(32) // Use 32 bytes for the challenge
        random.nextBytes(challenge)


        // Encode the challenge as a URL-safe base64 string
        val challengeBase64: String? =
            Base64.getUrlEncoder().withoutPadding().encodeToString(challenge)

        val userId = generateUserIdBase64() // TODO: Check if userId is correct
        return """ {
  "challenge": $challengeBase64,
  "rp": {
    "name": "Credential Manager example",
    "id": "credential-manager-test.example.com"
  },
  "user": {
    "id": $userId,
    "name": $name,
    "displayName": $email
  },
  "pubKeyCredParams": [
    {
      "type": "public-key",
      "alg": -7
    },
    {
      "type": "public-key",
      "alg": -257
    }
  ],
  "timeout": 1800000,
  "attestation": "none",
  "excludeCredentials": [
    {
      "id": "ghi789",
      "type": "public-key"
    },
    {
      "id": "jkl012",
      "type": "public-key"
    }
  ],
  "authenticatorSelection": {
    "authenticatorAttachment": "platform",
    "requireResidentKey": true,
    "residentKey": "required",
    "userVerification": "required"
  }
}""".trim()

    }

    fun createGoogleAuthenticationFlow(context: Context, credentialManager: CredentialManager) {

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(credentialOption = getSignInWithGoogleOption()).build()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = credentialManager.getCredential(
                    request = request, context = context
                )
                handleSignIn(result = result)
            } catch (e: GetCredentialException) {
                if (e is GetCredentialInterruptedException) {
                    // Try again in case of an interruption
                    //  createGoogleAuthenticationFlow(context = context, credentialManager)
                }
            }
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
        var currentAuthenticationAlternative = ""
        val phoneNumber = ""
        var userId = ""
        var email = ""
        val password = ""
        var userAvatarUri = ""

        val credential = result.credential
        when (credential) {
            is PublicKeyCredential -> {
                credential.authenticationResponseJson
                // Share responseJson i.e. a GetCredentialResponse on your server to
                // validate and  authenticate
            }

            is PasswordCredential -> {
                credential.id
                credential.password
                // Use id and password to send to your server to validate
                // and authenticate
            }


            is CustomCredential -> {
                when (credential.type) {
                    GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                        val result = GoogleIdTokenCredential.createFrom(credential.data)
                        val payload = verifyGoogleIdToken(result.idToken)
                        userId = payload.subject
                        email = payload.email
                        userAvatarUri = payload.get("picture") as String? ?: ""
                        currentAuthenticationAlternative = AuthenticationAlternative.GOOGLE.name
                    }
                }
            }
        }
        // TODO: Check if this is still necessary after using autofill in #108 Email and password authentication
        viewModelScope.launch {
            userPreferencesRepository.updateUserPreferences(
                currentAuthenticationAlternative = currentAuthenticationAlternative,
                phoneNumber = phoneNumber,
                userId = userId,
                email = email,
                password = password,
                userAvatarUri = userAvatarUri
            )
        }
    }


    fun verifyGoogleIdToken(idTokenString: String): GoogleIdToken.Payload {
        val transport = NetHttpTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()

        val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(Collections.singletonList(WEB_CLIENT_ID)).build()
        return verifier.verify(idTokenString).payload
    }

    @SuppressLint("PublicKeyCredential")
    fun createPasskey(
        requestJson: String, credentialManager: CredentialManager, activityContext: Context
    ) {
        val createPublicKeyCredentialRequest = CreatePublicKeyCredentialRequest(
            requestJson = requestJson,
        )

        viewModelScope.launch {
            try {
                credentialManager.createCredential(
                    request = createPublicKeyCredentialRequest,
                    context = activityContext,
                )
                //  handlePasskeyRegistrationResult(result)
            } catch (e: CreateCredentialException) {
                handleFailure(e)
            }
        }
    }


    fun handleFailure(e: CreateCredentialException) {
        when (e) {
            is CreatePublicKeyCredentialDomException -> {
                // Handle the passkey DOM errors thrown according to the
                // WebAuthn spec.
            }

            is CreateCredentialCancellationException -> {
                // The user intentionally canceled the operation and chose not
                // to register the credential.
            }

            is CreateCredentialInterruptedException -> {
                // Retry-able error. Consider retrying the call.
            }

            is CreateCredentialProviderConfigurationException -> {
                // Your app is missing the provider configuration dependency.
                // Most likely, you're missing the
                // "credentials-play-services-auth" module.
            }

            is CreateCredentialCustomException -> {
                // You have encountered an error from a 3rd-party SDK. If you
                // make the API call with a request object that's a subclass of
                // CreateCustomCredentialRequest using a 3rd-party SDK, then you
                // should check for any custom exception type constants within
                // that SDK to match with e.type. Otherwise, drop or log the
                // exception.
            }

            else -> Log.w(TAG, "Unexpected exception type ${e::class.java.name}")
        }
    }

}

sealed interface AuthenticationUiState {
    object Loading : AuthenticationUiState

    data class SignedIn(
        val currentAuthenticationAlternative: AuthenticationAlternative? = null,
        val phoneNumber: String? = null,
        val userId: String,
        val email: String? = null,
        val password: String? = null,
        val userAvatarUri: Uri? = null
    ) : AuthenticationUiState

    data class NotSignedIn(
        val userHasAccount: Boolean = false,
    ) : AuthenticationUiState
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
    val contentDescriptionResId: Int, val lightIconResId: Int, val darkIconResId: Int
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
    ),
    PASSKEY(
        contentDescriptionResId = R.string.authenticate_with_passkey,
        lightIconResId = R.drawable.baseline_vpn_key_24,
        darkIconResId = R.drawable.baseline_vpn_key_24
    )
}

