package com.darlingson.ndalamagoals.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.darlingson.ndalamagoals.data.appViewModel

private val DarkBackground = Color(0xFF0A1A17)
private val CardBackground = Color(0xFF1E3A35)
private val AccentGreen = Color(0xFF00C4B4)
private val TopBarColor = Color(0xFF0D1F1C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGoalsScreen(navController: NavHostController, mainViewModel: appViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Goals", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { /* if needed */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TopBarColor)
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tabs
            item {
                TabRow(
                    selectedTabIndex = 0, // Active selected
                    containerColor = CardBackground,
                    contentColor = AccentGreen
                ) {
                    listOf("Active", "Paused", "Completed").forEach { title ->
                        Tab(
                            selected = title == "Active",
                            onClick = { /* switch tab */ },
                            text = { Text(title) }
                        )
                    }
                }
            }

            // Summary Cards
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Card(colors = CardDefaults.cardColors(containerColor = CardBackground), modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("TOTAL SAVED", color = Color.Gray, fontSize = 12.sp)
                            Text("$12,450", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                    }
                    Card(colors = CardDefaults.cardColors(containerColor = CardBackground), modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("GOALS ACTIVE", color = Color.Gray, fontSize = 12.sp)
                            Text("4 / 6", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                    }
                }
            }

            item { Text("ACTIVE GOALS", color = Color.White, fontWeight = FontWeight.Medium) }

            // Goal Items
            item { GoalListItem(icon = Icons.Default.Warning, name = "Emergency Fund", target = "$10,000", current = "$6,500", progress = 0.65f, status = "On Track") }
            item { GoalListItem(icon = Icons.Default.Flight, name = "Japan Trip", target = "$3,000", current = "$2,850", progress = 0.95f, due = "By Dec 2024") }
            item { GoalListItem(icon = Icons.Default.Computer, name = "MacBook Pro", target = "$2,500", current = "$800", progress = 0.32f) }
            item { GoalListItem(icon = Icons.Default.SportsEsports, name = "PS5 Games", target = "$500", current = "$60", progress = 0.12f) }

            item { Spacer(Modifier.height(80.dp)) } // FAB space
        }
    }
}

@Composable
fun GoalListItem(icon: ImageVector, name: String, target: String, current: String, progress: Float, due: String? = null, status: String? = "On Track") {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        onClick = { /* navigate to detail */ },
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
                        AssistChip(onClick = {}, label = { Text(status) }, colors = AssistChipDefaults.assistChipColors(containerColor = AccentGreen.copy(0.2f), labelColor = AccentGreen))
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