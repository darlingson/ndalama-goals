package com.darlingson.ndalamagoals.data

import androidx.room.TypeConverter
import com.darlingson.ndalamagoals.data.entities.GoalType

class Converters {
    @TypeConverter
    fun fromGoalType(goalType: GoalType): String {
        return goalType.name
    }

    @TypeConverter
    fun toGoalType(goalTypeString: String): GoalType {
        return GoalType.valueOf(goalTypeString)
    }
}