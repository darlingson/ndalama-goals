package com.darlingson.ndalamagoals.data.repositories

import com.darlingson.ndalamagoals.data.daos.ContributionDao
import com.darlingson.ndalamagoals.data.entities.Contribution
import kotlinx.coroutines.flow.Flow

class ContributionRepository(private val contributionDao: ContributionDao) {
    val allContributions: Flow<List<Contribution>> = contributionDao.getAllContributions()

    suspend fun insert(contribution: Contribution) {
        contributionDao.insertContribution(contribution)
    }
    suspend fun deleteAll() {
        contributionDao.deleteAll()
    }
}