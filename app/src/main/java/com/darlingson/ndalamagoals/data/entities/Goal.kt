package com.darlingson.ndalamagoals.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val desc: String,
    val target: Double,
    val date: Long,
    val isPriority: Boolean,
    val isPrivate: Boolean,
    val contributionFrequency: String,
    val targetDate: Long,
    val status: String //active, inactive, completed, paused
)
