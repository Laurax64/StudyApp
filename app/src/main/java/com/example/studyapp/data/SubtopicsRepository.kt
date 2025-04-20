package com.example.studyapp.data

import kotlinx.coroutines.flow.Flow

interface SubtopicsRepository {

    suspend fun insertSubtopic(subtopic: Subtopic)

    suspend fun deleteSubtopic(subtopic: Subtopic)

    suspend fun updateSubtopic(subtopic: Subtopic)

    fun getSubtopic(id: Int): Flow<Subtopic>

    fun getAllSubtopics(topicId: Int): Flow<List<Subtopic>>
}