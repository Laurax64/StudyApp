package com.example.studyapp.di

import android.content.Context
import androidx.room.Room
import com.example.studyapp.data.AppDatabase
import com.example.studyapp.data.SubtopicDao
import com.example.studyapp.data.SubtopicsRepository
import com.example.studyapp.data.SubtopicsRepositoryImpl
import com.example.studyapp.data.TopicDao
import com.example.studyapp.data.TopicsRepository
import com.example.studyapp.data.TopicsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Modules {
    @Provides
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

}