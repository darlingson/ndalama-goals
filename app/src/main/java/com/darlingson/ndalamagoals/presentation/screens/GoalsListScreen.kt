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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.darlingson.ndalamagoals.presentation.components.GoalItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsListScreen(navController: NavHostController) {
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
                        Text("$14,500", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Spacer(Modifier.width(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.weight(1f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        Text("GOAL TARGET", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        Text("$32,000", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            Text("Priority Goal", color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(16.dp))
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Placeholder for cylinder progress (simplified with LinearProgress)
                        CircularProgressIndicator(
                            progress = 0.8f,
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 8.dp,
                            modifier = Modifier.size(80.dp)
                        )
                        Text("80%", color = MaterialTheme.colorScheme.onSurface)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text("Emergency Fund", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        Text("$8,000 / $10,000", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                            Text("+")
                        }
                    }
                }
            }

            Row(modifier = Modifier.padding(16.dp)) {
                Text("Active Goals", color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                Text("View All", color = MaterialTheme.colorScheme.primary)
            }

            LazyColumn {
                item {
                    GoalItem(
                        icon = Icons.Default.Share, // Plane icon placeholder
                        name = "Japan Trip",
                        amount = "$1,200",
                        target = "$6,000",
                        due = "Due Dec 20 2024",
                        progress = 0.2f,
                        status = "On Track",
                        onClick = { navController.navigate("goal_detail") }
                    )
                }
                item {
                    GoalItem(
                        icon = Icons.Default.Share, // Laptop icon placeholder
                        name = "New Laptop",
                        amount = "$500",
                        target = "$1,500",
                        due = "Need ASAP",
                        progress = 0.33f,
                        status = "Behind",
                        onClick = {}
                    )
                }
            }

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
