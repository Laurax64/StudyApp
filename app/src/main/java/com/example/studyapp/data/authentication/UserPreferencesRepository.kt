package com.example.studyapp.data.authentication

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class UserPreferences(
    val currentAuthenticationAlternative: String? = null,
    val phoneNumber: String? = null,
    val userId: String?,
    val email: String? = null,
    val password: String? = null,
    val userAvatarUri: String? = null
)

/**
 * Class that handles saving and retrieving user preferences
 */
class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_AVATAR_URI = stringPreferencesKey("user_avatar_uri")
        val CURRENT_AUTHENTICATION_ALTERNATIVE =
            stringPreferencesKey("current_authentication_alternative")
        val PHONE_NUMBER = stringPreferencesKey("phone_number")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        mapUserPreferences(preferences)
    }

    suspend fun updateUserPreferences(
        currentAuthenticationAlternative: String,
        phoneNumber: String,
        userId: String,
        email: String,
        password: String,
        userAvatarUri: String,
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_AUTHENTICATION_ALTERNATIVE] =
                currentAuthenticationAlternative
            preferences[PreferencesKeys.PHONE_NUMBER] = phoneNumber
            preferences[PreferencesKeys.USER_ID] = userId
            preferences[PreferencesKeys.EMAIL] = email
            preferences[PreferencesKeys.PASSWORD] = password
            preferences[PreferencesKeys.USER_AVATAR_URI] = userAvatarUri
        }
    }

    suspend fun updateUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }

    suspend fun updateUserAvatarUri(avatarUri: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_AVATAR_URI] = avatarUri
        }
    }

    suspend fun updateCurrentAuthenticationAlternative(currentAuthenticationAlternative: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_AUTHENTICATION_ALTERNATIVE] =
                currentAuthenticationAlternative
        }
    }

    suspend fun updatePhoneNumber(phoneNumber: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PHONE_NUMBER] = phoneNumber
        }
    }

    suspend fun updateEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.EMAIL] = email
        }
    }

    suspend fun updatePassword(password: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PASSWORD] = password
        }
    }

    suspend fun clearUserPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        return UserPreferences(
            currentAuthenticationAlternative = preferences[PreferencesKeys.CURRENT_AUTHENTICATION_ALTERNATIVE],
            phoneNumber = preferences[PreferencesKeys.PHONE_NUMBER],
            email = preferences[PreferencesKeys.EMAIL],
            password = preferences[PreferencesKeys.PASSWORD],
            userId = preferences[PreferencesKeys.USER_ID],
            userAvatarUri = preferences[PreferencesKeys.USER_AVATAR_URI]
        )
    }
}