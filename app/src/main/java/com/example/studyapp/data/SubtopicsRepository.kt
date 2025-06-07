package com.example.studyapp.data

import kotlinx.coroutines.flow.Flow

interface SubtopicsRepository {

    suspend fun insertSubtopic(subtopic: Subtopic)

    suspend fun deleteSubtopic(id: Int)

    suspend fun updateSubtopic(subtopic: Subtopic)

    fun getSubtopic(id: Int): Flow<Subtopic?>

    fun getAllSubtopics(topicId: Int): Flow<List<Subtopic>>

    fun deleteAssociatedSubtopics(topicId: Int)
}