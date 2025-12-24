package com.darlingson.ndalamagoals.presentation.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.runtime.getValue

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

    val progress = getProgressPercentage(goal)
    val savedAmount = goal.target * progress

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
                onClick = {navController.navigate("add_contribution")},
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
                    Text("On track to reach goal by deadline.", color = MaterialTheme.colorScheme.primary)
                }
            }

            Row(modifier = Modifier.padding(16.dp)) {
                Button(onClick = {}, modifier = Modifier.weight(1f)) { Icon(Icons.Default.Share, contentDescription = null); Text("Link Entry") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {}, modifier = Modifier.weight(1f)) { Icon(Icons.Default.Edit, contentDescription = null); Text("Edit Goal") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {}, modifier = Modifier.weight(1f)) { Icon(Icons.Default.Pause, contentDescription = null); Text("Pause") }
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

private fun getProgressPercentage(goal: Goal): Float {
    val daysSinceCreation = (System.currentTimeMillis() - goal.date) / (1000 * 60 * 60 * 24)
    val totalDays = (goal.targetDate - goal.date) / (1000 * 60 * 60 * 24)
    return if (totalDays > 0) (daysSinceCreation.toFloat() / totalDays.toFloat()).coerceAtMost(1f) else 0f
}

private fun formatDate(timestamp: Long): String {
    return if (timestamp != 0L) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        dateFormat.format(Date(timestamp))
    } else {
        "No due date"
    }
}