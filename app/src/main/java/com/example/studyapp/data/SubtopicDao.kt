package com.example.studyapp.data

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    suspend fun delete(subtopic: Subtopic)

    @Query("SELECT * from subtopics WHERE id = :id")
    fun getSubtopic(id: Int): Flow<Subtopic?>

    @Query("SELECT * from subtopics WHERE topicId = :topicId")
    fun getAllSubtopics(topicId: Int): Flow<List<Subtopic>>

}