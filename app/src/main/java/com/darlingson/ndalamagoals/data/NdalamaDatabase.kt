package com.darlingson.ndalamagoals.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.darlingson.ndalamagoals.data.daos.ContributionDao
import com.darlingson.ndalamagoals.data.daos.GoalDao
import com.darlingson.ndalamagoals.data.entities.Contribution
import com.darlingson.ndalamagoals.data.entities.Goal

@Database(entities = [Goal::class, Contribution::class], version = 1, exportSchema = false)
abstract class NdalamaDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun contributionDao(): ContributionDao

    companion object {
        @Volatile
        private var INSTANCE: NdalamaDatabase? = null

        fun getDatabase(context: Context): NdalamaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NdalamaDatabase::class.java,
                    "ndalama_goals_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
