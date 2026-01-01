package com.darlingson.ndalamagoals.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.darlingson.ndalamagoals.data.appViewModel
import com.darlingson.ndalamagoals.data.entities.Goal
import com.darlingson.ndalamagoals.data.entities.Contribution
import com.darlingson.ndalamagoals.presentation.components.ContributionItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import androidx.compose.runtime.getValue
import com.darlingson.ndalamagoals.presentation.components.AnimatedActionButton
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(navController: NavHostController, mainViewModel: appViewModel, goalId: Int?) {
    val goals by mainViewModel.allGoals.collectAsState(initial = emptyList())
    val contributions by mainViewModel.allContributions.collectAsState(initial = emptyList())

    val goal = goals.firstOrNull { it.id == goalId }
    val goalContributions = contributions.filter { it.goalId == goalId }.sortedByDescending { it.date }

    if (goal == null) {
        Text("Goal not found", modifier = Modifier.fillMaxSize())
        return
    }

    // Calculate advanced progress based on contribution frequency
    val progressData = calculateGoalProgress(goal, goalContributions)
    val progress = progressData.progress
    val savedAmount = progressData.savedAmount
    val expectedAmount = progressData.expectedAmount
    val status = progressData.status
    val buttonSpring = spring<Float>(
        stiffness = Spring.StiffnessLow,
        dampingRatio = Spring.DampingRatioMediumBouncy
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(goal.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Edit, contentDescription = null) }
                    IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = null) }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("add_contribution/${goal.id}") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Contribution") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.padding(16.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(60.dp))
                    Text("Target Date: ${formatDate(goal.targetDate)}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(16.dp))
                    Text("CURRENT BALANCE", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$${String.format("%.0f", savedAmount)} / $${goal.target}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("${(progress * 100).toInt()}% Achieved", color = MaterialTheme.colorScheme.primary)
                    Text("$${String.format("%.0f", goal.target - savedAmount)} left", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    LinearProgressIndicator(progress = progress, color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth())
                    Text("Status: $status", color = MaterialTheme.colorScheme.primary)
                    Text("purpose of goal: ${goal.goalPurpose}")
                    Text("Goal type: ${goal.goalType}")

                    // Show expected vs actual
                    if (expectedAmount > 0) {
                        Text(
                            "Expected: $${String.format("%.0f", expectedAmount)} | Behind by: $${String.format("%.0f", (expectedAmount - savedAmount).coerceAtLeast(0.0))}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Column {

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    AnimatedActionButton(
                        onClick = { navController.navigate("edit_goal/${goal.id}") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Edit")
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = spring(
                                stiffness = Spring.StiffnessLow,
                                dampingRatio = Spring.DampingRatioNoBouncy
                            )
                        )
                ) {

                    AnimatedVisibility(
                        visible = goal.status == "active",
                        enter = fadeIn(animationSpec = buttonSpring) +
                                slideInVertically(initialOffsetY = { it / 3 }),
                        exit = fadeOut(animationSpec = buttonSpring) +
                                slideOutVertically(targetOffsetY = { it / 3 })
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {

                            AnimatedActionButton(
                                onClick = { mainViewModel.pauseGoal(goal.id) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Pause, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Pause")
                            }

                            Spacer(Modifier.width(8.dp))

                            AnimatedActionButton(
                                onClick = { mainViewModel.completeGoal(goal.id) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Complete")
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = goal.status != "active",
                        enter = fadeIn(animationSpec = buttonSpring) +
                                scaleIn(initialScale = 0.9f, animationSpec = buttonSpring),
                        exit = fadeOut(animationSpec = buttonSpring) +
                                scaleOut(targetScale = 0.9f, animationSpec = buttonSpring)
                    ) {
                        AnimatedActionButton(
                            onClick = { mainViewModel.activateGoal(goal.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Activate")
                        }
                    }
                }
            }



            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Contributions History", color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                Text("View Analytics", color = MaterialTheme.colorScheme.primary)
            }

            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(goalContributions) { contribution ->
                    ContributionItem(
                        amount = "+$${contribution.amount}",
                        type = contribution.type,
                        desc = contribution.desc,
                        date = formatDate(contribution.date)
                    )
                }
            }

            Text("Goal started on ${formatDate(goal.date)}", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(16.dp))
        }
    }
}

private fun calculateGoalProgress(goal: Goal, contributions: List<Contribution>): GoalProgressData {
    val currentTime = System.currentTimeMillis()
    val totalDuration = goal.targetDate - goal.date
    val elapsedDuration = currentTime - goal.date

    if (totalDuration <= 0) return GoalProgressData(0f, 0.0, 0.0, "Invalid timeline")

    // Calculate expected contributions based on frequency
    val expectedContributions = calculateExpectedContributions(goal, currentTime)
    val expectedAmount = expectedContributions * calculateContributionAmount(goal)

    // Calculate actual progress
    val savedAmount = contributions.sumOf { it.amount }
    val progress = (savedAmount / goal.target).toFloat().coerceIn(0f, 1f)

    // Determine status
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
    val totalDuration = goal.targetDate - startDate
    val elapsedDuration = currentTime - startDate

    val frequency = goal.contributionFrequency.lowercase()
    val totalPeriods = when (frequency) {
        "daily" -> TimeUnit.MILLISECONDS.toDays(totalDuration)
        "weekly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 7
        "bi-weekly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 14
        "monthly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 30
        "bi-monthly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 60
        "tri-monthly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 90
        "quarterly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 90
        "6 months" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 180
        "yearly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 365
        else -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 30 // Default to monthly
    }

    val elapsedPeriods = when (frequency) {
        "daily" -> TimeUnit.MILLISECONDS.toDays(elapsedDuration)
        "weekly" -> TimeUnit.MILLISECONDS.toDays(elapsedDuration) / 7
        "bi-weekly" -> TimeUnit.MILLISECONDS.toDays(elapsedDuration) / 14
        "monthly" -> TimeUnit.MILLISECONDS.toDays(elapsedDuration) / 30
        "bi-monthly" -> TimeUnit.MILLISECONDS.toDays(elapsedDuration) / 60
        "tri-monthly" -> TimeUnit.MILLISECONDS.toDays(elapsedDuration) / 90
        "quarterly" -> TimeUnit.MILLISECONDS.toDays(elapsedDuration) / 90
        "6 months" -> TimeUnit.MILLISECONDS.toDays(elapsedDuration) / 180
        "yearly" -> TimeUnit.MILLISECONDS.toDays(elapsedDuration) / 365
        else -> TimeUnit.MILLISECONDS.toDays(elapsedDuration) / 30 // Default to monthly
    }

    return elapsedPeriods.toInt().coerceAtLeast(0)
}

private fun calculateContributionAmount(goal: Goal): Double {
    val frequency = goal.contributionFrequency.lowercase()
    val totalDuration = goal.targetDate - goal.date

    val totalPeriods = when (frequency) {
        "daily" -> TimeUnit.MILLISECONDS.toDays(totalDuration)
        "weekly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 7
        "bi-weekly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 14
        "monthly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 30
        "bi-monthly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 60
        "tri-monthly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 90
        "quarterly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 90
        "6 months" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 180
        "yearly" -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 365
        else -> TimeUnit.MILLISECONDS.toDays(totalDuration) / 30 // Default to monthly
    }

    return if (totalPeriods > 0) goal.target / totalPeriods else goal.target
}

private fun formatDate(timestamp: Long): String {
    return if (timestamp != 0L) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        dateFormat.format(Date(timestamp))
    } else {
        "No due date"
    }
}

data class GoalProgressData(
    val progress: Float,
    val savedAmount: Double,
    val expectedAmount: Double,
    val status: String
)