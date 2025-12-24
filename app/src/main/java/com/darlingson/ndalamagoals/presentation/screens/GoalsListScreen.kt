package com.darlingson.ndalamagoals.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.darlingson.ndalamagoals.data.appViewModel
import com.darlingson.ndalamagoals.data.entities.Goal
import com.darlingson.ndalamagoals.presentation.components.GoalItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsListScreen(navController: NavHostController, mainViewModel: appViewModel) {
    val goals by mainViewModel.allGoals.collectAsState(initial = emptyList())

    val totalSaved = goals.sumOf { it.target * getProgressPercentage(it) }
    val totalTarget = goals.sumOf { it.target }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Goals") },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Lock, contentDescription = "Private")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("create_goal") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add New Goal") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.weight(1f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        Text("TOTAL SAVED", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        Text("$${String.format("%.0f", totalSaved)}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Spacer(Modifier.width(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.weight(1f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        Text("GOAL TARGET", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        Text("$${String.format("%.0f", totalTarget)}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            val priorityGoal = goals.firstOrNull { it.isPriority }
            if (priorityGoal != null) {
                Text("Priority Goal", color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            val progress = getProgressPercentage(priorityGoal)
                            CircularProgressIndicator(
                                progress = progress,
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 8.dp,
                                modifier = Modifier.size(80.dp)
                            )
                            Text("${(progress * 100).toInt()}%", color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(priorityGoal.name, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                            Text("$${getSavedAmount(priorityGoal)} / $${priorityGoal.target}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                                Text("+")
                            }
                        }
                    }
                }
            }

            Row(modifier = Modifier.padding(16.dp)) {
                Text("Active Goals", color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                Text("${goals.filter { it.status == "active" }.size} goals", color = MaterialTheme.colorScheme.primary)
            }

            LazyColumn {
                items(goals.filter { it.status == "active" }) { goal ->
                    GoalItem(
                        icon = Icons.Default.Share,
                        name = goal.name,
                        amount = "$${getSavedAmount(goal)}",
                        target = "$${goal.target}",
                        due = "Due ${formatDate(goal.targetDate)}",
                        progress = getProgressPercentage(goal),
                        status = getGoalStatus(goal),
                        onClick = { navController.navigate("goal_detail/${goal.id}") }
                    )
                }
            }

            if (goals.isEmpty()) {
                Card(modifier = Modifier.padding(16.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Have a new dream?", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

private fun getProgressPercentage(goal: Goal): Float {
    // This is a placeholder - you might want to track actual savings/contributions
    // For now, using a random progress based on creation date
    val daysSinceCreation = (System.currentTimeMillis() - goal.date) / (1000 * 60 * 60 * 24)
    val totalDays = (goal.targetDate - goal.date) / (1000 * 60 * 60 * 24)
    return if (totalDays > 0) (daysSinceCreation.toFloat() / totalDays.toFloat()).coerceAtMost(1f) else 0f
}

private fun getSavedAmount(goal: Goal): String {
    val progress = getProgressPercentage(goal)
    return String.format("%.0f", goal.target * progress)
}

private fun formatDate(timestamp: Long): String {
    return if (timestamp != 0L) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        dateFormat.format(Date(timestamp))
    } else {
        "No due date"
    }
}

private fun getGoalStatus(goal: Goal): String {
    val progress = getProgressPercentage(goal)
    return when {
        progress >= 1f -> "Completed"
        progress > 0.8f -> "Almost There"
        progress > 0.5f -> "On Track"
        progress > 0.3f -> "Behind"
        else -> "Need Attention"
    }
}