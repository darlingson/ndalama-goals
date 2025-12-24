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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalScreen(navController: NavHostController, mainViewModel: appViewModel) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var creationTimestamp by remember { mutableStateOf(0L) }
    var targetTimestamp by remember { mutableStateOf(0L) }
    var isPriority by remember { mutableStateOf(false) }
    var isPrivate by remember { mutableStateOf(false) }
    var frequency by remember { mutableStateOf("Monthly") }
    var status by remember { mutableStateOf("active") }

    // Date picker states
    var showCreationDatePicker by remember { mutableStateOf(false) }
    var showTargetDatePicker by remember { mutableStateOf(false) }

    // Dropdown states
    var frequencyExpanded by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    val frequencyOptions = listOf(
        "Daily", "Weekly", "Bi-weekly", "Monthly", "Bi-monthly",
        "Tri-monthly", "Quarterly", "6 months", "Yearly"
    )

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
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Goal Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = target, onValueChange = { target = it }, label = { Text("Target Amount") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = if (creationTimestamp != 0L) dateFormat.format(Date(creationTimestamp)) else "",
                onValueChange = { },
                label = { Text("Goal StartDate") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showCreationDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Select Date"
                        )
                    }
                }
            )

            // Target Date Picker
            OutlinedTextField(
                value = if (targetTimestamp != 0L) dateFormat.format(Date(targetTimestamp)) else "",
                onValueChange = { },
                label = { Text("Target Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showTargetDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Select Date"
                        )
                    }
                }
            )

            ExposedDropdownMenuBox(
                expanded = frequencyExpanded,
                onExpandedChange = { frequencyExpanded = !frequencyExpanded }
            ) {
                OutlinedTextField(
                    value = frequency,
                    onValueChange = { },
                    label = { Text("Frequency") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = frequencyExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = frequencyExpanded,
                    onDismissRequest = { frequencyExpanded = false }
                ) {
                    frequencyOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                frequency = option
                                frequencyExpanded = false
                            }
                        )
                    }
                }
            }

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
                        date = creationTimestamp,
                        isPrivate = isPrivate,
                        isPriority = isPriority,
                        frequency = frequency,
                        targetDate = targetTimestamp,
                    )
                    navController.popBackStack()
                }
            ) {
                Text("Save Goal")
            }
        }
    }

    if (showCreationDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showCreationDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showCreationDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreationDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(
                initialDisplayMode = DisplayMode.Picker
            )
            DatePicker(state = datePickerState)

            datePickerState.selectedDateMillis?.let { millis ->
                creationTimestamp = millis
            }
        }
    }

    if (showTargetDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showTargetDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showTargetDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTargetDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(
                initialDisplayMode = DisplayMode.Picker
            )
            DatePicker(state = datePickerState)

            datePickerState.selectedDateMillis?.let { millis ->
                targetTimestamp = millis
            }
        }
    }
}