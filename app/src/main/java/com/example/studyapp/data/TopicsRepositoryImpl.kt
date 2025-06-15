package com.example.studyapp.data

import kotlinx.coroutines.flow.Flow

class TopicsRepositoryImpl(private val topicDao: TopicDao) : TopicsRepository {
    override suspend fun insertTopic(topic: Topic) = topicDao.insert(topic = topic)

    override suspend fun deleteTopic(topicId: Int) = topicDao.delete(topicId = topicId)

    override suspend fun updateTopic(topic: Topic) = topicDao.update(topic = topic)

    override fun getTopic(id: Int): Flow<Topic?> = topicDao.getTopic(topicId = id)

    override fun getAllTopics(): Flow<List<Topic>> = topicDao.getAllTopics()
}