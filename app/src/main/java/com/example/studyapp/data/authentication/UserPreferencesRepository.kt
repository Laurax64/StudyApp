package com.example.studyapp.data.authentication

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

data class UserPreferences(
    val userId: String?
)

/**
 * Class that handles saving and retrieving user preferences
 */
class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val USER_ID = stringPreferencesKey("user_id")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        mapUserPreferences(preferences)
    }

    suspend fun updateUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())


    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val userInitial = preferences[PreferencesKeys.USER_ID]
        return UserPreferences(userInitial)
    }
}