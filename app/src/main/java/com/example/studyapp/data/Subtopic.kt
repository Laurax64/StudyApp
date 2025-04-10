package com.example.studyapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtopics")
data class Subtopic(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val imageUri: String?
)