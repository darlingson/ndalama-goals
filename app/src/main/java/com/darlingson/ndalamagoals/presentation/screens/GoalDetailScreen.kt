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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.darlingson.ndalamagoals.data.appViewModel
import com.darlingson.ndalamagoals.presentation.components.ContributionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(navController: NavHostController, mainViewModel: appViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Japan Trip") },
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
                    Text("Target Date: Dec 25, 2024", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(16.dp))
                    Text("CURRENT BALANCE", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$1,200 / $6,000", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("20% Achieved", color = MaterialTheme.colorScheme.primary)
                    Text("$4,800 left", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    LinearProgressIndicator(progress = 0.2f, color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth())
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
                item {
                    ContributionItem(amount = "+$400.00", type = "Monthly Saving", desc = "Manual Deposit", date = "Oct 24, 2023")
                }
                item {
                    ContributionItem(amount = "+$600.00", type = "Bonus Allocation", desc = "From 'Salary Bonus'", date = "Oct 15, 2023")
                }
                item {
                    ContributionItem(amount = "+$200.00", type = "Initial Deposit", desc = "Goal Created", date = "Sep 01, 2023")
                }
            }

            Text("Goal started on Sep 01, 2023", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(16.dp))
        }
    }
}