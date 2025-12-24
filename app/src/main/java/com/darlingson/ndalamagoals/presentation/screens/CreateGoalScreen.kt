package com.darlingson.ndalamagoals.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.darlingson.ndalamagoals.data.appViewModel
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material.icons.automirrored.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalScreen(navController: NavHostController, mainViewModel: appViewModel) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var isPriority by remember { mutableStateOf(false) }
    var isPrivate by remember { mutableStateOf(false) }
    var frequency by remember { mutableStateOf("Monthly") }
    var targetDate by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("active") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("New Goal Entry") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Text Inputs
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Goal Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = target, onValueChange = { target = it }, label = { Text("Target Amount") }, modifier = Modifier.fillMaxWidth())

            // Date Inputs (Manual Long/String)
            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Creation Date (Long/Timestamp)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = targetDate, onValueChange = { targetDate = it }, label = { Text("Target Date (Long/Timestamp)") }, modifier = Modifier.fillMaxWidth())

            // Dropdowns/Selectors
            OutlinedTextField(value = frequency, onValueChange = { frequency = it }, label = { Text("Frequency (Weekly/Monthly)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = status, onValueChange = { status = it }, label = { Text("Status (active/paused/completed)") }, modifier = Modifier.fillMaxWidth())

            // Booleans
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Private Goal")
                Switch(checked = isPrivate, onCheckedChange = { isPrivate = it })
                Spacer(Modifier.width(16.dp))
                Text("Priority Goal")
                Switch(
                    checked = isPriority, onCheckedChange = { isPriority = it },
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    mainViewModel.addGoal(
                        name = name,
                        desc = desc,
                        target = target.toDoubleOrNull() ?: 0.0,
                        date = date.toLongOrNull() ?: 0L,
                        isPrivate = isPrivate,
                        isPriority = isPriority,
                        frequency = frequency,
                        targetDate = targetDate.toLongOrNull() ?: 0L,
                    )
                    navController.popBackStack()
                }
            ) {
                Text("Save Goal")
            }
        }
    }
}