package com.example.studyapp.data.study

import kotlinx.coroutines.flow.Flow

interface TopicsRepository {

    suspend fun insertTopic(topic: Topic)

    suspend fun deleteTopic(topicId: Int)

    suspend fun updateTopic(topic: Topic)

    fun getTopic(id: Int): Flow<Topic?>

    fun getAllTopics(): Flow<List<Topic>>
}
