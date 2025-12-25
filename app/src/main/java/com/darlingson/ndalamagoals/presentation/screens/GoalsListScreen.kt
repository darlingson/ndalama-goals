package com.darlingson.ndalamagoals.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.darlingson.ndalamagoals.data.entities.Contribution
import com.darlingson.ndalamagoals.presentation.components.GoalItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.graphicsLayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsListScreen(navController: NavHostController, mainViewModel: appViewModel) {
    val goals by mainViewModel.allGoals.collectAsState(initial = emptyList())
    val contributions by mainViewModel.allContributions.collectAsState(initial = emptyList())

    // Calculate totals using sophisticated progress calculation
    val totalSaved = goals.sumOf { goal ->
        val goalContributions = contributions.filter { it.goalId == goal.id }
        calculateGoalProgress(goal, goalContributions).savedAmount
    }
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
            // 1. State to track if the menu is expanded
            var isFabExpanded by remember { mutableStateOf(false) }

            // 2. Use a Column to stack the sub-buttons ABOVE the main button
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(bottom = 16.dp) // Space from bottom of screen
            ) {

                // 3. Secondary Buttons (Animated)
                AnimatedVisibility(
                    visible = isFabExpanded,
                    enter = slideInVertically(initialOffsetY = { it }) + expandVertically(expandFrom = Alignment.Bottom) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut()
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(12.dp) // Space between sub-buttons
                    ) {

                        // --- Option 1: All Goals ---
                        // WhatsApp style: Text label on the left, Button on the right
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Label
                            Text(
                                text = "All Goals",
                                modifier = Modifier.padding(end = 12.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                            // Small Button
                            SmallFloatingActionButton(
                                onClick = {
                                    isFabExpanded = false // Close menu
                                    navController.navigate("my_goals")
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = null)
                            }
                        }

                        // --- Option 2: Add New Goal ---
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Add New Goal",
                                modifier = Modifier.padding(end = 12.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                            SmallFloatingActionButton(
                                onClick = {
                                    isFabExpanded = false
                                    navController.navigate("create_goal")
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                        }
                    }
                }

                // 4. Main Button
                FloatingActionButton(
                    onClick = { isFabExpanded = !isFabExpanded },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    // Rotate the icon 45 degrees when expanded to look like an "X"
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Menu",
                        modifier = Modifier.graphicsLayer {
                            rotationZ = if (isFabExpanded) 45f else 0f
                        }
                    )
                }
            }
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
                val progressData = calculateGoalProgress(priorityGoal, contributions.filter { it.goalId == priorityGoal.id })
                Text("Priority Goal", color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {

                            CircularProgressIndicator(
                                progress = progressData.progress,
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 8.dp,
                                modifier = Modifier.size(80.dp)
                            )
                            Text("${(progressData.progress * 100).toInt()}%", color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(priorityGoal.name, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                            Text("$${String.format("%.0f", progressData.savedAmount)} / $${priorityGoal.target}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Status: ${progressData.status}", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
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
                    val goalContributions = contributions.filter { it.goalId == goal.id }
                    val progressData = calculateGoalProgress(goal, goalContributions)

                    GoalItem(
                        icon = Icons.Default.Share,
                        name = goal.name,
                        amount = "$${String.format("%.0f", progressData.savedAmount)}",
                        target = "$${goal.target}",
                        due = "Due ${formatDate(goal.targetDate)}",
                        progress = progressData.progress,
                        status = progressData.status,
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

private fun calculateGoalProgress(goal: Goal, contributions: List<Contribution>): GoalProgressData {
    val currentTime = System.currentTimeMillis()
    val totalDuration = goal.targetDate - goal.date
    val elapsedDuration = currentTime - goal.date

    if (totalDuration <= 0) return GoalProgressData(0f, 0.0, 0.0, "Invalid timeline")

    // Calculate actual saved amount
    val savedAmount = contributions.sumOf { it.amount }

    // Calculate expected contributions based on frequency
    val expectedContributions = calculateExpectedContributions(goal, currentTime)
    val contributionAmount = calculateContributionAmount(goal)
    val expectedAmount = expectedContributions * contributionAmount

    // Calculate progress based on target completion
    val progress = (savedAmount / goal.target).toFloat().coerceIn(0f, 1f)

    // Determine status based on expected vs actual
    val status = when {
        progress >= 1f -> "Completed"
        savedAmount >= expectedAmount -> "On Track"
        savedAmount >= expectedAmount * 0.8 -> "Slightly Behind"
        savedAmount >= expectedAmount * 0.5 -> "Behind"
        else -> "Significantly Behind"
    }

    return GoalProgressData(progress, savedAmount, expectedAmount, status)
}

private fun calculateExpectedContributions(goal: Goal, currentTime: Long): Int {
    val startDate = goal.date
    val elapsedDuration = currentTime - startDate

    if (elapsedDuration <= 0) return 0

    val frequency = goal.contributionFrequency.lowercase()
    val daysElapsed = TimeUnit.MILLISECONDS.toDays(elapsedDuration)

    return when (frequency) {
        "daily" -> daysElapsed
        "weekly" -> daysElapsed / 7
        "bi-weekly" -> daysElapsed / 14
        "monthly" -> daysElapsed / 30
        "bi-monthly" -> daysElapsed / 60
        "tri-monthly" -> daysElapsed / 90
        "quarterly" -> daysElapsed / 90
        "6 months" -> daysElapsed / 180
        "yearly" -> daysElapsed / 365
        else -> daysElapsed / 30 // Default to monthly
    }.toInt().coerceAtLeast(0)
}

private fun calculateContributionAmount(goal: Goal): Double {
    val frequency = goal.contributionFrequency.lowercase()
    val totalDuration = goal.targetDate - goal.date

    if (totalDuration <= 0) return goal.target

    val totalDays = TimeUnit.MILLISECONDS.toDays(totalDuration)

    val totalPeriods = when (frequency) {
        "daily" -> totalDays
        "weekly" -> totalDays / 7
        "bi-weekly" -> totalDays / 14
        "monthly" -> totalDays / 30
        "bi-monthly" -> totalDays / 60
        "tri-monthly" -> totalDays / 90
        "quarterly" -> totalDays / 90
        "6 months" -> totalDays / 180
        "yearly" -> totalDays / 365
        else -> totalDays / 30 // Default to monthly
    }.toInt().coerceAtLeast(1)

    return goal.target / totalPeriods
}

private fun formatDate(timestamp: Long): String {
    return if (timestamp != 0L) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        dateFormat.format(Date(timestamp))
    } else {
        "No due date"
    }
}
//
//private data class GoalProgressData(
//    val progress: Float,
//    val savedAmount: Double,
//    val expectedAmount: Double,
//    val status: String
//)