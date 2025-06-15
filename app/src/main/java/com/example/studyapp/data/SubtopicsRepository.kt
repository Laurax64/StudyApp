package com.example.studyapp.data

import kotlinx.coroutines.flow.Flow

interface SubtopicsRepository {

    suspend fun insertSubtopic(subtopic: Subtopic)

    suspend fun deleteSubtopic(subtopicId: Int)

    suspend fun updateSubtopic(subtopic: Subtopic)

    fun getSubtopic(subtopicId: Int): Flow<Subtopic?>

    fun getAllSubtopics(): Flow<List<Subtopic>>

    suspend fun deleteAssociatedSubtopics(topicId: Int)
}