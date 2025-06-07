package com.example.studyapp.data

import kotlinx.coroutines.flow.Flow

interface TopicsRepository {

    suspend fun insertTopic(topic: Topic)

    suspend fun deleteTopic(id: Int)

    suspend fun updateTopic(topic: Topic)

    fun getTopic(id: Int): Flow<Topic?>

    fun getAllTopics(): Flow<List<Topic>>
}
