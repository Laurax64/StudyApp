package com.example.studyapp.data.study

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtopics")
data class Subtopic(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String = "",
    val topicId: Int,
    val title: String,
    val description: String,
    val checked: Boolean,
    val bookmarked: Boolean,
    val imageUri: String?
)