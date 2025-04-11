package com.example.studyapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class Topic(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val checked: Boolean
)