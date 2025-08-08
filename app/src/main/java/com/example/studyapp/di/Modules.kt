package com.example.studyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.studyapp.data.AppDatabase
import com.example.studyapp.data.authentication.UserPreferencesRepository
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
    fun providesAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database",
    ).build()

    @Provides
    fun provideTopicDao(appDatabase: AppDatabase) = appDatabase.topicDao()

    @Provides
    fun provideSubtopicDao(appDatabase: AppDatabase) = appDatabase.subtopicDao()

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