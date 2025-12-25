package com.darlingson.ndalamagoals.data.repositories

import com.darlingson.ndalamagoals.data.daos.GoalDao
import kotlinx.coroutines.flow.Flow
import com.darlingson.ndalamagoals.data.entities.Goal

class GoalRepository(private val goalDao: GoalDao) {
    val allGoals: Flow<List<Goal>> = goalDao.getAllGoals()

    suspend fun insert(goal: Goal) {
        goalDao.insertGoal(goal)
    }
    suspend fun update(goal: Goal) {
        goalDao.updateGoal(goal)
    }
}