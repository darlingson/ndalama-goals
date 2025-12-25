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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalScreen(navController: NavHostController, mainViewModel: appViewModel, goalId: Int?) {
    var goalName by remember { mutableStateOf("Yosemite Cabin Fund") }
    var targetAmount by remember { mutableStateOf("12000.00") }
    var monthlyContribution by remember { mutableStateOf("500.00") }
    var frequency by remember { mutableStateOf("Monthly") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Goal", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Lock, contentDescription = "Private", tint = AccentGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TopBarColor)
            )
        },
        containerColor = DarkBackground,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* save */ },
                containerColor = AccentGreen,
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Save Changes", fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Icon Selector
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.size(100.dp).clip(RoundedCornerShape(16.dp)).background(CardBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Landscape, contentDescription = null, tint = Color.White, modifier = Modifier.size(60.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Change icon", color = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.Edit, contentDescription = null, tint = AccentGreen)
                    }
                }
            }

            // Goal Name
            item {
                OutlinedTextField(
                    value = goalName,
                    onValueChange = { goalName = it },
                    label = { Text("Goal Name") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Target Amount
            item {
                OutlinedTextField(
                    value = targetAmount,
                    onValueChange = { targetAmount = it },
                    label = { Text("Target Amount") },
                    prefix = { Text("$ ") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Frequency Dropdown
            item {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = frequency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Frequency") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),

                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf("Weekly", "Monthly", "Yearly").forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = { frequency = it; expanded = false })
                        }
                    }
                }
            }

            // Monthly Contribution
            item {
                OutlinedTextField(
                    value = monthlyContribution,
                    onValueChange = { monthlyContribution = it },
                    label = { Text("Monthly Contribution") },
                    prefix = { Text("$ ") },
                    modifier = Modifier.fillMaxWidth(),

                )
            }

            // Dates
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = "01/15/2023",
                        onValueChange = {},
                        label = { Text("Start Date") },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.CalendarToday, null) },
                        modifier = Modifier.weight(1f),
                    )
                    OutlinedTextField(
                        value = "12/31/2024",
                        onValueChange = {},
                        label = { Text("Target Date") },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.CalendarToday, null) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            item {
                Card(colors = CardDefaults.cardColors(containerColor = AccentGreen.copy(alpha = 0.2f))) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = AccentGreen)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("You are on track!", color = AccentGreen, fontWeight = FontWeight.Bold)
                            Text("At this rate, you will reach your goal of $12,000.00 by Dec 2024.", color = Color.White)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}