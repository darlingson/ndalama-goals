package com.darlingson.ndalamagoals.data

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

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

    fun exportDataToCSV(context: Context) {
        viewModelScope.launch {
            try {
                val goals = allGoals.value
                val contributions = allContributions.value

                // Debug logging
                android.util.Log.d("Export", "Goals count: ${goals.size}")
                android.util.Log.d("Export", "Contributions count: ${contributions.size}")

                if (goals.isEmpty() && contributions.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        android.widget.Toast.makeText(context, "No data to export", android.widget.Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                val csvContent = buildString {
                    appendLine("GOALS DATA")
                    appendLine("ID,Name,Description,Target,Date,IsPriority,IsPrivate,Frequency,TargetDate,Status,Type,Purpose")
                    goals.forEach { goal ->
                        appendLine("${goal.id},\"${goal.name}\",\"${goal.desc}\",${goal.target},${goal.date},${goal.isPriority},${goal.isPrivate},${goal.contributionFrequency},${goal.targetDate},${goal.status},${goal.goalType},${goal.goalPurpose}")
                    }

                    appendLine()
                    appendLine("CONTRIBUTIONS DATA")
                    appendLine("ID,Amount,Type,Description,Date,GoalID,Source")
                    contributions.forEach { contribution ->
                        appendLine("${contribution.id},${contribution.amount},${contribution.type},\"${contribution.desc}\",${contribution.date},${contribution.goalId},${contribution.source}")
                    }
                }

                val timestamp = System.currentTimeMillis()
                val filename = "ndalama_goals_export_${timestamp}.csv"

                android.util.Log.d("Export", "Creating file: $filename")

                val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val values = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                        put(MediaStore.MediaColumns.DATE_ADDED, timestamp / 1000)
                        put(MediaStore.MediaColumns.DATE_MODIFIED, timestamp / 1000)
                    }

                    val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                    context.contentResolver.insert(collection, values)
                } else {
                    // Pre-Android 10 approach
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadsDir, filename)

                    FileOutputStream(file).use { outputStream ->
                        outputStream.write(csvContent.toByteArray())
                    }

                    // Notify media scanner
                    MediaScannerConnection.scanFile(
                        context,
                        arrayOf(file.absolutePath),
                        arrayOf("text/csv"),
                        null
                    )

                    android.net.Uri.fromFile(file)
                }

                uri?.let {
                    try {
                        context.contentResolver.openOutputStream(it)?.use { outputStream ->
                            outputStream.write(csvContent.toByteArray())
                            outputStream.flush()
                        }

                        android.util.Log.d("Export", "File written successfully")

                        withContext(Dispatchers.Main) {
                            android.widget.Toast.makeText(context, "Data exported to Downloads folder", android.widget.Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("Export", "Error writing file: ${e.message}")
                        throw e
                    }
                } ?: run {
                    android.util.Log.e("Export", "Failed to create file URI")
                    throw Exception("Could not create file")
                }

            } catch (e: Exception) {
                android.util.Log.e("Export", "Export failed: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    android.widget.Toast.makeText(context, "Export failed: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun wipeAllData(context: Context) {
        viewModelScope.launch {
            try {
                goalRepository.deleteAll()
                contributionRepository.deleteAll()

                withContext(Dispatchers.Main) {
                    android.widget.Toast.makeText(context, "All data has been wiped", android.widget.Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    android.widget.Toast.makeText(context, "Wipe failed: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}