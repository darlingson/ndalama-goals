package com.darlingson.ndalamagoals.data

import com.darlingson.ndalamagoals.data.repositories.ContributionRepository
import com.darlingson.ndalamagoals.data.repositories.GoalRepository
import com.darlingson.ndalamagoals.data.entities.Contribution
import com.darlingson.ndalamagoals.data.entities.Goal
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class appViewModel(private val contributionRepository: ContributionRepository, private val goalRepository: GoalRepository) : ViewModel() {

    val allContributions: StateFlow<List<Contribution>> = contributionRepository.allContributions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allGoals: StateFlow<List<Goal>> = goalRepository.allGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun addContribution(amount: Double, type: String, desc: String, date: Long, goalId: Int, source: String) {
        viewModelScope.launch {
            val newContribution = Contribution(
                amount = amount,
                type = type,
                desc = desc,
                date = date,
                goalId = goalId,
                source = source
            )
            contributionRepository.insert(newContribution)
        }
    }

    fun addGoal(name: String, desc: String, date: Long, isPrivate: Boolean?, isPriority: Boolean?, frequency: String, targetDate: Long, target: Double) {
        viewModelScope.launch {
            val newGoal = Goal(
                name = name,
                desc = desc,
                target = target,
                date = date,
                isPriority = isPriority == true,
                isPrivate = isPrivate == true,
                contributionFrequency = frequency,
                targetDate = targetDate,
                status = "active"
            )
            goalRepository.insert(newGoal)
        }
    }
    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            goalRepository.update(goal)
        }
    }
}