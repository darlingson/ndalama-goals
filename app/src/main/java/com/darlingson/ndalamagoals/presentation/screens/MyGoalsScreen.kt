package com.darlingson.ndalamagoals.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.darlingson.ndalamagoals.data.appViewModel
import com.darlingson.ndalamagoals.data.entities.Goal
import com.darlingson.ndalamagoals.data.entities.Contribution
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import androidx.compose.runtime.getValue

private val DarkBackground = Color(0xFF0A1A17)
private val CardBackground = Color(0xFF1E3A35)
private val AccentGreen = Color(0xFF00C4B4)
private val TopBarColor = Color(0xFF0D1F1C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGoalsScreen(navController: NavHostController, mainViewModel: appViewModel) {
    val goals by mainViewModel.allGoals.collectAsState(initial = emptyList())
    val contributions by mainViewModel.allContributions.collectAsState(initial = emptyList())

    var selectedTab by remember { mutableStateOf("Active") }

    // Calculate totals using sophisticated progress calculation
    val totalSaved = goals.sumOf { goal ->
        val goalContributions = contributions.filter { it.goalId == goal.id }
        calculateGoalProgress(goal, goalContributions).savedAmount
    }
    val activeGoals = goals.filter { it.status == "active" }.size
    val totalGoals = goals.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Goals", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* search functionality could be added here */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TopBarColor)
            )
        },
        containerColor = DarkBackground,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("create_goal") },
                containerColor = AccentGreen,
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("New Goal", fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TabRow(
                    selectedTabIndex = when (selectedTab) {
                        "Active" -> 0
                        "Paused" -> 1
                        "Completed" -> 2
                        else -> 0
                    },
                    containerColor = CardBackground,
                    contentColor = AccentGreen
                ) {
                    listOf("Active", "Paused", "Completed").forEach { title ->
                        Tab(
                            selected = selectedTab == title,
                            onClick = { selectedTab = title },
                            text = { Text(title, color = if (selectedTab == title) AccentGreen else Color.Gray) }
                        )
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Card(colors = CardDefaults.cardColors(containerColor = CardBackground), modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("TOTAL SAVED", color = Color.Gray, fontSize = 12.sp)
                            Text("$${String.format("%,.0f", totalSaved)}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                    }
                    Card(colors = CardDefaults.cardColors(containerColor = CardBackground), modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("GOALS ACTIVE", color = Color.Gray, fontSize = 12.sp)
                            Text("$activeGoals / $totalGoals", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                    }
                }
            }

            item {
                Text("${selectedTab.uppercase()} GOALS", color = Color.White, fontWeight = FontWeight.Medium)
            }

            // Filter goals based on selected tab
            val filteredGoals = goals.filter { goal ->
                when (selectedTab) {
                    "Active" -> goal.status == "active"
                    "Paused" -> goal.status == "paused"
                    "Completed" -> goal.status == "completed"
                    else -> goal.status == "active"
                }
            }

            items(filteredGoals) { goal ->
                val goalContributions = contributions.filter { it.goalId == goal.id }
                val progressData = calculateGoalProgress(goal, goalContributions)
                val icon = getGoalIcon(goal.name)

                GoalListItem(
                    icon = icon,
                    name = goal.name,
                    target = "$${String.format("%,.0f", goal.target)}",
                    current = "$${String.format("%,.0f", progressData.savedAmount)}",
                    progress = progressData.progress,
                    due = "Due ${formatDate(goal.targetDate)}",
                    status = progressData.status,
                    onClick = { navController.navigate("goal_detail/${goal.id}") }
                )
            }

            if (filteredGoals.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.AddCircle, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(16.dp))
                            Text("No ${selectedTab.lowercase()} goals", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Create a new goal to get started", color = Color.Gray)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun GoalListItem(
    icon: ImageVector,
    name: String,
    target: String,
    current: String,
    progress: Float,
    due: String? = null,
    status: String? = "On Track",
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(40.dp))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(name, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    if (status != null) {
                        AssistChip(
                            onClick = {},
                            label = { Text(status) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = getStatusColor(status).copy(0.2f),
                                labelColor = getStatusColor(status)
                            )
                        )
                    }
                }
                Text("Target: $target", color = Color.Gray, fontSize = 12.sp)
                if (due != null) Text(due, color = Color.Gray, fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                Text("Current", color = Color.Gray, fontSize = 12.sp)
                Text(current, color = Color.White, fontWeight = FontWeight.Bold)
                LinearProgressIndicator(
                    progress = { progress },
                    color = AccentGreen,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                )
                Text("${(progress * 100).toInt()}%", color = AccentGreen, modifier = Modifier.align(Alignment.End))
            }
        }
    }
}

// Helper functions
private fun getGoalIcon(goalName: String): ImageVector {
    return when {
        goalName.contains("Emergency", ignoreCase = true) -> Icons.Default.Warning
        goalName.contains("Trip", ignoreCase = true) || goalName.contains("Travel", ignoreCase = true) -> Icons.Default.Flight
        goalName.contains("Computer", ignoreCase = true) || goalName.contains("Laptop", ignoreCase = true) || goalName.contains("MacBook", ignoreCase = true) -> Icons.Default.Computer
        goalName.contains("Game", ignoreCase = true) || goalName.contains("PS5", ignoreCase = true) || goalName.contains("Gaming", ignoreCase = true) -> Icons.Default.SportsEsports
        goalName.contains("Car", ignoreCase = true) || goalName.contains("Vehicle", ignoreCase = true) -> Icons.Default.DirectionsCar
        goalName.contains("House", ignoreCase = true) || goalName.contains("Home", ignoreCase = true) -> Icons.Default.Home
        goalName.contains("Wedding", ignoreCase = true) -> Icons.Default.Favorite
        goalName.contains("Education", ignoreCase = true) || goalName.contains("School", ignoreCase = true) -> Icons.Default.School
        else -> Icons.Default.Savings
    }
}

private fun getStatusColor(status: String): Color {
    return when (status) {
        "Completed" -> Color(0xFF4CAF50)
        "On Track" -> AccentGreen
        "Slightly Behind" -> Color(0xFFFFA726)
        "Behind" -> Color(0xFFFF9800)
        "Significantly Behind" -> Color(0xFFF44336)
        else -> AccentGreen
    }
}

private fun formatDate(timestamp: Long): String {
    return if (timestamp != 0L) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        "Due ${dateFormat.format(Date(timestamp))}"
    } else {
        "No due date"
    }
}

// Reuse the sophisticated progress calculation from before
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
