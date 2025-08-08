package com.example.studyapp.data.study

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(topic: Topic)

    @Update
    suspend fun update(topic: Topic)

    @Query("DELETE from topics WHERE id = :topicId")
    suspend fun delete(topicId: Int)

    @Query("SELECT * from topics WHERE id = :topicId")
    fun getTopic(topicId: Int): Flow<Topic?>

    @Query("SELECT * from topics")
    fun getAllTopics(): Flow<List<Topic>>
}