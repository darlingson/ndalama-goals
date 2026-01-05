package com.darlingson.ndalamagoals.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.darlingson.ndalamagoals.data.entities.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY id DESC")
    fun getAllGoals(): Flow<List<Goal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Query("UPDATE goals SET status = 'paused' WHERE id = :goalId")
    suspend fun pauseGoal(goalId: Int)

    @Query("UPDATE goals SET status = 'completed' WHERE id = :goalId")
    suspend fun complete(goalId: Int)

    @Query("UPDATE goals SET status = 'active' WHERE id = :goalId")
    suspend fun activate(goalId: Int)
    @Query("DELETE FROM goals")
    suspend fun deleteAll()
}
