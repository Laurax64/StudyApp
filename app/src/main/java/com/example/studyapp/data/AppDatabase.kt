package com.example.studyapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.studyapp.data.study.Subtopic
import com.example.studyapp.data.study.SubtopicDao
import com.example.studyapp.data.study.Topic
import com.example.studyapp.data.study.TopicDao

@Database(entities = [Topic::class, Subtopic::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun subtopicDao(): SubtopicDao
}