package com.darlingson.ndalamagoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.darlingson.ndalamagoals.ui.theme.NdalamaGoalsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NdalamaGoalsTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "goals_list") {
                    composable("goals_list") { GoalsListScreen(navController) }
                    composable("goal_detail") { GoalDetailScreen(navController) }
                    composable("create_goal") { CreateGoalScreen(navController) }
                }
            }
        }
    }
}

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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D1F1C))
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("create_goal") },
                containerColor = Color(0xFF00C4B4),
                contentColor = Color.Black,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add New Goal") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFF0A1A17))) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A35)), modifier = Modifier.weight(1f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        Text("TOTAL SAVED", color = Color.Gray, fontSize = 12.sp)
                        Text("$14,500", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
                Spacer(Modifier.width(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A35)), modifier = Modifier.weight(1f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        Text("GOAL TARGET", color = Color.Gray, fontSize = 12.sp)
                        Text("$32,000", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
            }

            Text("Priority Goal", color = Color.White, modifier = Modifier.padding(16.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A35)), modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF0D2A25)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Placeholder for cylinder progress (simplified with LinearProgress)
                        CircularProgressIndicator(
                            progress = 0.8f,
                            color = Color(0xFF00C4B4),
                            strokeWidth = 8.dp,
                            modifier = Modifier.size(80.dp)
                        )
                        Text("80%", color = Color.White)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text("Emergency Fund", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("$8,000 / $10,000", color = Color.Gray)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C4B4))) {
                            Text("+")
                        }
                    }
                }
            }

            Row(modifier = Modifier.padding(16.dp)) {
                Text("Active Goals", color = Color.White, modifier = Modifier.weight(1f))
                Text("View All", color = Color(0xFF00C4B4))
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

            Card(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF00C4B4))
                    Spacer(Modifier.width(8.dp))
                    Text("Have a new dream?", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun GoalItem(icon: ImageVector, name: String, amount: String, target: String, due: String, progress: Float, status: String, onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A35)), modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp).clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color(0xFF00C4B4), modifier = Modifier.size(40.dp))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, color = Color.White, fontWeight = FontWeight.Bold)
                Text("$amount of $target", color = Color.Gray)
                LinearProgressIndicator(progress = progress, color = if (status == "On Track") Color.Green else Color.Red, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
                Text(due, color = Color.Gray, fontSize = 12.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(status, color = if (status == "On Track") Color.Green else Color.Red)
                Text("Update Balance", color = Color(0xFF00C4B4), fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(navController: NavHostController) {
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D1F1C))
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {},
                containerColor = Color(0xFF00C4B4),
                contentColor = Color.Black,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Contribution") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFF0A1A17))) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A35)), modifier = Modifier.padding(16.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF00C4B4), modifier = Modifier.size(60.dp))
                    Text("Target Date: Dec 25, 2024", color = Color.Gray)
                    Spacer(Modifier.height(16.dp))
                    Text("CURRENT BALANCE", color = Color.Gray)
                    Text("$1,200 / $6,000", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text("20% Achieved", color = Color(0xFF00C4B4))
                    Text("$4,800 left", color = Color.Gray)
                    LinearProgressIndicator(progress = 0.2f, color = Color(0xFF00C4B4), modifier = Modifier.fillMaxWidth())
                    Text("On track to reach goal by deadline.", color = Color.Green)
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
                Text("Contributions History", color = Color.White, modifier = Modifier.weight(1f))
                Text("View Analytics", color = Color(0xFF00C4B4))
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

            Text("Goal started on Sep 01, 2023", color = Color.Gray, modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun ContributionItem(amount: String, type: String, desc: String, date: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Box(modifier = Modifier.size(40.dp).background(Color(0xFF1E3A35), CircleShape), contentAlignment = Alignment.Center) {
            when {
                amount.startsWith("+") -> Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF00C4B4))
                amount.startsWith("-") -> Icon(Icons.Default.Add, contentDescription = null, tint = Color.Red) // Placeholder
                else -> Icon(Icons.Default.Add, contentDescription = null, tint = Color.Gray)
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(type, color = Color.White, fontWeight = FontWeight.Bold)
            Text(desc, color = Color.Gray)
            Text(date, color = Color.Gray, fontSize = 12.sp)
        }
        Text(amount, color = if (amount.startsWith("+")) Color(0xFF00C4B4) else Color.Red, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Goal") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Lock, contentDescription = "Private")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D1F1C))
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFF0A1A17)).padding(16.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A35)), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(60.dp).border(2.dp, Color(0xFF00C4B4), CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF00C4B4), modifier = Modifier.size(40.dp)) // Target icon placeholder
                    }
                    Spacer(Modifier.width(16.dp))
                    Text("Choose an icon", color = Color.White)
                }
            }

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Goal Name") }, placeholder = { Text("e.g. Dream Vacation") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Target Amount") }, placeholder = { Text("$ 0.00") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Target Date (OPTIONAL)") }, placeholder = { Text("mm/dd/yyyy") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Text("We'll help calculate your monthly contribution.", color = Color.Gray)

            Spacer(Modifier.height(64.dp))

            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C4B4))) {
                Text("Save Goal", color = Color.Black)
            }
        }
    }
}