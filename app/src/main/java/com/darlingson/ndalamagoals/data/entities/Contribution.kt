package com.darlingson.ndalamagoals.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contributions")
data class Contribution(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val type: String,
    val desc: String,
    val date: Long,
    val goalId: Int,
    val source: String
)
