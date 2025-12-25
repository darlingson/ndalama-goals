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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.darlingson.ndalamagoals.data.appViewModel
import com.darlingson.ndalamagoals.data.entities.Goal
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalScreen(navController: NavHostController, mainViewModel: appViewModel, goalId: Int?) {
    val goals by mainViewModel.allGoals.collectAsState()
    val goal = goals.firstOrNull { it.id == goalId }

    var goalName by remember { mutableStateOf(goal?.name ?: "") }
    var targetAmount by remember { mutableStateOf(goal?.target?.toString() ?: "") }
    var frequency by remember { mutableStateOf(goal?.contributionFrequency ?: "Monthly") }
    var description by remember { mutableStateOf(goal?.desc ?: "") }
    var startDate by remember { mutableStateOf(goal?.date ?: System.currentTimeMillis()) }
    var targetDate by remember { mutableStateOf(goal?.targetDate ?: System.currentTimeMillis()) }
    var isPrivate by remember { mutableStateOf(goal?.isPrivate ?: false) }
    var isPriority by remember { mutableStateOf(goal?.isPriority ?: false) }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showTargetDatePicker by remember { mutableStateOf(false) }
    var showIconPicker by remember { mutableStateOf(false) }
    var selectedIcon by remember { mutableStateOf(Icons.Default.Landscape) }

    val frequencyOptions = listOf("Daily", "Weekly", "Bi-weekly", "Monthly", "Bi-monthly", "Tri-monthly", "Quarterly", "6 months", "Yearly")
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    val scope = rememberCoroutineScope()

    LaunchedEffect(goal) {
        goal?.let {
            goalName = it.name
            targetAmount = it.target.toString()
            frequency = it.contributionFrequency
            description = it.desc
            startDate = it.date
            targetDate = it.targetDate
            isPrivate = it.isPrivate
            isPriority = it.isPriority
        }
    }

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
                    IconButton(onClick = { isPrivate = !isPrivate }) {
                        Icon(
                            if (isPrivate) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = "Private",
                            tint = AccentGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TopBarColor)
            )
        },
        containerColor = DarkBackground,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        )
        {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.size(100.dp).clip(RoundedCornerShape(16.dp)).background(CardBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(selectedIcon, contentDescription = null, tint = Color.White, modifier = Modifier.size(60.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = { showIconPicker = true }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Change icon", color = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Default.Edit, contentDescription = null, tint = AccentGreen)
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = goalName,
                    onValueChange = { goalName = it },
                    label = { Text("Goal Name", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        unfocusedBorderColor = CardBackground,
                        focusedLabelColor = AccentGreen,
                        unfocusedLabelColor = Color.White,
                        cursorColor = AccentGreen
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        unfocusedBorderColor = CardBackground,
                        focusedLabelColor = AccentGreen,
                        unfocusedLabelColor = Color.White,
                        cursorColor = AccentGreen
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = targetAmount,
                    onValueChange = { targetAmount = it },
                    label = { Text("Target Amount", color = Color.White) },
                    prefix = { Text("$ ", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        unfocusedBorderColor = CardBackground,
                        focusedLabelColor = AccentGreen,
                        unfocusedLabelColor = Color.White,
                        cursorColor = AccentGreen
                    )
                )
            }

            item {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = frequency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Contribution Frequency", color = Color.White) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentGreen,
                            unfocusedBorderColor = CardBackground,
                            focusedLabelColor = AccentGreen,
                            unfocusedLabelColor = Color.White,
                            cursorColor = AccentGreen
                        )
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        frequencyOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.White) },
                                onClick = { frequency = option; expanded = false }
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Priority Goal", color = Color.White, fontWeight = FontWeight.Medium)
                    Switch(
                        checked = isPriority,
                        onCheckedChange = { isPriority = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AccentGreen,
                            checkedTrackColor = AccentGreen.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = dateFormat.format(Date(startDate)),
                        onValueChange = {},
                        label = { Text("Start Date", color = Color.White) },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showStartDatePicker = true }) {
                                Icon(Icons.Default.CalendarToday, null, tint = AccentGreen)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentGreen,
                            unfocusedBorderColor = CardBackground,
                            focusedLabelColor = AccentGreen,
                            unfocusedLabelColor = Color.White,
                            cursorColor = AccentGreen
                        )
                    )
                    OutlinedTextField(
                        value = dateFormat.format(Date(targetDate)),
                        onValueChange = {},
                        label = { Text("Target Date", color = Color.White) },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showTargetDatePicker = true }) {
                                Icon(Icons.Default.CalendarToday, null, tint = AccentGreen)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentGreen,
                            unfocusedBorderColor = CardBackground,
                            focusedLabelColor = AccentGreen,
                            unfocusedLabelColor = Color.White,
                            cursorColor = AccentGreen
                        )
                    )
                }
            }

            item {
                Card(colors = CardDefaults.cardColors(containerColor = AccentGreen.copy(alpha = 0.2f))) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = AccentGreen)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Goal Progress", color = AccentGreen, fontWeight = FontWeight.Bold)
                            Text("Update your goal details to stay on track.", color = Color.White)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }
            item {
                Button(
                    onClick = {
                        scope.launch {
                            if (goal != null && goalName.isNotBlank() && targetAmount.toDoubleOrNull() != null) {
                                val updatedGoal = goal.copy(
                                    name = goalName,
                                    target = targetAmount.toDouble(),
                                    contributionFrequency = frequency,
                                    desc = description,
                                    date = startDate,
                                    targetDate = targetDate,
                                    isPrivate = isPrivate,
                                    isPriority = isPriority
                                )
                                mainViewModel.updateGoal(updatedGoal)
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentGreen,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("OK", color = AccentGreen)
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("Cancel", color = Color.White)
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = startDate)
            DatePicker(state = datePickerState)
            datePickerState.selectedDateMillis?.let { startDate = it }
        }
    }

    if (showTargetDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showTargetDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showTargetDatePicker = false }) {
                    Text("OK", color = AccentGreen)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTargetDatePicker = false }) {
                    Text("Cancel", color = Color.White)
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = targetDate)
            DatePicker(state = datePickerState)
            datePickerState.selectedDateMillis?.let { targetDate = it }
        }
    }
}

// Colors
private val DarkBackground = Color(0xFF0A1A17)
private val CardBackground = Color(0xFF1E3A35)
private val AccentGreen = Color(0xFF00C4B4)
private val TopBarColor = Color(0xFF0D1F1C)