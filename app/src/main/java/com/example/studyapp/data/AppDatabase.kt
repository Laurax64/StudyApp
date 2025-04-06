package com.example.studyapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Topic::class], [Subtopic::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun subtopicDao(): SubtopicDao
}
