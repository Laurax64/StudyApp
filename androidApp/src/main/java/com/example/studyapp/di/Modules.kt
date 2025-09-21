package com.example.studyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.shared.Database
import com.example.shared.data.authentication.UserPreferencesRepository
import com.example.studyapp.data.study.SubtopicDao
import com.example.studyapp.data.study.SubtopicsRepository
import com.example.studyapp.data.study.SubtopicsRepositoryImpl
import com.example.studyapp.data.study.TopicDao
import com.example.studyapp.data.study.TopicsRepository
import com.example.studyapp.data.study.TopicsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "user_preferences"

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): Database {
        val dbFile = context.getDatabasePath("app-database.db")
        return Room.databaseBuilder<Database>(context, dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    @Provides
    fun provideTopicDao(database: Database) = database.topicDao()

    @Provides
    fun provideSubtopicDao(database: Database) = database.subtopicDao()

    @Provides
    fun provideTopicsRepository(topicDao: TopicDao): TopicsRepository =
        TopicsRepositoryImpl(topicDao = topicDao)

    @Provides
    fun provideSubtopicsRepository(subtopicDao: SubtopicDao): SubtopicsRepository =
        SubtopicsRepositoryImpl(subtopicDao = subtopicDao)

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        preferencesDataStore(name = USER_PREFERENCES_NAME).getValue(context, context::javaClass)


    @Provides
    @Singleton
    fun providesUserPreferencesRepository(dataStore: DataStore<Preferences>) =
        UserPreferencesRepository(dataStore = dataStore)

}