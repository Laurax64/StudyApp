package com.example.studyapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SubtopicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(subtopic: Subtopic)

    @Update
    suspend fun update(subtopic: Subtopic)

    @Query("DELETE from subtopics WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * from subtopics WHERE id = :id")
    fun getSubtopic(id: Int): Flow<Subtopic?>

    @Query("SELECT * from subtopics")
    fun getAllSubtopics(topicId: Int): Flow<List<Subtopic>>

    @Query("DELETE from subtopics WHERE topicId = :topicId")
    suspend fun deleteAssociatedSubtopics(topicId: Int)

}