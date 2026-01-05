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
import com.darlingson.ndalamagoals.data.entities.GoalType
import com.darlingson.ndalamagoals.data.repositories.SettingsRepository

class appViewModel(private val contributionRepository: ContributionRepository, private val goalRepository: GoalRepository, private val repository: SettingsRepository) : ViewModel() {

    val allContributions: StateFlow<List<Contribution>> = contributionRepository.allContributions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allGoals: StateFlow<List<Goal>> = goalRepository.allGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val settings: StateFlow<Settings> =
        repository.settingsFlow
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                Settings()
            )

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

    fun addGoal(name: String, desc: String, date: Long, isPrivate: Boolean?, isPriority: Boolean?, frequency: String, targetDate: Long, target: Double, type: String = "SAVINGS") {
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
                status = "active",
                goalType = GoalType.valueOf(type.uppercase()),
                goalPurpose = type
            )
            goalRepository.insert(newGoal)
        }
    }
    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            goalRepository.update(goal)
        }
    }
    fun pauseGoal(goalId: Int){
        viewModelScope.launch {
            goalRepository.pause(goalId)
        }
    }

    fun completeGoal(goalId: Int){
        viewModelScope.launch {
            goalRepository.complete(goalId)
        }
    }

    fun activateGoal(goalId: Int){
        viewModelScope.launch {
            goalRepository.activate(goalId)
        }
    }

    fun setCurrency(currency: String) =
        viewModelScope.launch {
            repository.updateCurrency(currency)
        }

    fun setFormat(format: Int) =
        viewModelScope.launch {
            repository.updateNumberFormat(format)
        }

    fun setBiometrics(enabled: Boolean) =
        viewModelScope.launch {
            repository.updateBiometrics(enabled)
        }
}