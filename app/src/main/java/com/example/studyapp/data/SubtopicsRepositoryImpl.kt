package com.example.studyapp.data

import kotlinx.coroutines.flow.Flow

class SubtopicsRepositoryImpl(private val subtopicDao: SubtopicDao) : SubtopicsRepository {
    override suspend fun insertSubtopic(subtopic: Subtopic) =
        subtopicDao.insert(subtopic = subtopic)

    override suspend fun deleteSubtopic(subtopicId: Int) =
        subtopicDao.delete(subtopicId = subtopicId)

    override suspend fun updateSubtopic(subtopic: Subtopic) =
        subtopicDao.update(subtopic = subtopic)

    override fun getSubtopic(subtopicId: Int): Flow<Subtopic?> =
        subtopicDao.getSubtopic(subtopicId = subtopicId)

    override fun getAllSubtopics(): Flow<List<Subtopic>> =
        subtopicDao.getAllSubtopics()

    override suspend fun deleteAssociatedSubtopics(topicId: Int) {
        subtopicDao.deleteAssociatedSubtopics(topicId = topicId)
    }
}
