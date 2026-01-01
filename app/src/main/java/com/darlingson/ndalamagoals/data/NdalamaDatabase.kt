package com.darlingson.ndalamagoals.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.darlingson.ndalamagoals.data.daos.ContributionDao
import com.darlingson.ndalamagoals.data.daos.GoalDao
import com.darlingson.ndalamagoals.data.entities.Contribution
import com.darlingson.ndalamagoals.data.entities.Goal

@Database(entities = [Goal::class, Contribution::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NdalamaDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun contributionDao(): ContributionDao

    companion object {
        @Volatile
        private var INSTANCE: NdalamaDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE goals ADD COLUMN goalType TEXT NOT NULL DEFAULT 'SAVINGS'"
                )
                database.execSQL(
                    "ALTER TABLE goals ADD COLUMN goalPurpose TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        fun getDatabase(context: Context): NdalamaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NdalamaDatabase::class.java,
                    "ndalama_goals_db"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
