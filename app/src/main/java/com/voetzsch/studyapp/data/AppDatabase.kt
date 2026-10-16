package com.voetzsch.studyapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.voetzsch.studyapp.data.study.Subtopic
import com.voetzsch.studyapp.data.study.SubtopicDao
import com.voetzsch.studyapp.data.study.Topic
import com.voetzsch.studyapp.data.study.TopicDao

@Database(entities = [Topic::class, Subtopic::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun subtopicDao(): SubtopicDao
}