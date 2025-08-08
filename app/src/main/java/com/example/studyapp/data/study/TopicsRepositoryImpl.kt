package com.example.studyapp.data.study

import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.VisibleForTesting

class TopicsRepositoryImpl(@VisibleForTesting val topicDao: TopicDao) : TopicsRepository {
    override suspend fun insertTopic(topic: Topic) = topicDao.insert(topic = topic)

    override suspend fun deleteTopic(topicId: Int) = topicDao.delete(topicId = topicId)

    override suspend fun updateTopic(topic: Topic) = topicDao.update(topic = topic)

    override fun getTopic(id: Int): Flow<Topic?> = topicDao.getTopic(topicId = id)

    override fun getAllTopics(): Flow<List<Topic>> = topicDao.getAllTopics()
}