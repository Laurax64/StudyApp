package com.example.studyapp.data

import kotlinx.coroutines.flow.Flow

class SubtopicsRepositoryImpl(private val subtopicDao: SubtopicDao) : SubtopicsRepository {
    override suspend fun insertSubtopic(subtopic: Subtopic) =
        subtopicDao.insert(subtopic = subtopic)

    override suspend fun deleteSubtopic(subtopic: Subtopic) =
        subtopicDao.delete(subtopic = subtopic)

    override suspend fun updateSubtopic(subtopic: Subtopic) =
        subtopicDao.update(subtopic = subtopic)

    override fun getSubtopic(id: Int): Flow<Subtopic?> = subtopicDao.getSubtopic(id = id)

    override fun getAllSubtopics(topicId: Int): Flow<List<Subtopic>> =
        subtopicDao.getAllSubtopics(topicId = topicId)
}
