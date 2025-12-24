package com.darlingson.ndalamagoals.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darlingson.ndalamagoals.ui.theme.NdalamaGoalsTheme

private val DarkBackground = Color(0xFF0A1A17)
private val CardBackground = Color(0xFF1E3A35)
private val AccentGreen = Color(0xFF00C4B4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContributionScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var amount by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf("Savings") }
    var description by remember { mutableStateOf("") }
    var additionalNotes by remember { mutableStateOf("") }

    val categories = listOf("Savings", "Income", "Gift")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Contribution", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D1F1C))
            )
        },
        containerColor = DarkBackground,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onSave,
                containerColor = AccentGreen,
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Save Contribution", fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Amount Section with big centered display
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("$", fontSize = 48.sp, color = AccentGreen, fontWeight = FontWeight.Light)
                    Text(
                        "$${amount}",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text("USD", color = Color.Gray, fontSize = 18.sp)

                    Spacer(Modifier.height(16.dp))

                    // Simple slider (replace with better one or custom if needed)
                    Slider(
                        value = amount.toFloat(),
                        onValueChange = { amount = it.toInt() },
                        valueRange = 0f..5000f,
                        steps = 99,
                        colors = SliderDefaults.colors(
                            thumbColor = AccentGreen,
                            activeTrackColor = AccentGreen
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Contributing To Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(AccentGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Flight,
                                contentDescription = null,
                                tint = AccentGreen,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("CONTRIBUTING TO", color = Color.Gray, fontSize = 12.sp)
                            Text("Japan Trip", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreen)
                    }
                }
            }

            // Category Chips
            item {
                Text("Category", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    categories.forEach { category ->
                        FilterChip(
                            onClick = { selectedCategory = category },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (category == "Savings") Icon(Icons.Default.Savings, contentDescription = null, modifier = Modifier.size(18.dp))
                                    else if (category == "Income") Icon(Icons.Default.Work, contentDescription = null, modifier = Modifier.size(18.dp))
                                    else Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(category)
                                }
                            },
                            selected = selectedCategory == category,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentGreen,
                                selectedLabelColor = Color.Black,
                                containerColor = CardBackground,
                                labelColor = Color.White
                            ),
                            border = null
                        )
                    }
                }
            }

            // Date Field
            item {
                OutlinedTextField(
                    value = "10/26/2023",
                    onValueChange = {},
                    label = { Text("Date", color = Color.Gray) },
                    leadingIcon = {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray)
                    },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Short Description
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description", color = Color.Gray) },
                    leadingIcon = {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Gray)
                    },
                    placeholder = { Text("Short description", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Additional Notes
            item {
                OutlinedTextField(
                    value = additionalNotes,
                    onValueChange = { additionalNotes = it },
                    label = { Text("Additional Notes", color = Color.Gray) },
                    leadingIcon = {
                        Icon(Icons.Default.Note, contentDescription = null, tint = Color.Gray)
                    },
                    placeholder = { Text("Add details about this contribution...", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),

                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Extra bottom space for FAB
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AddContributionScreenPreview() {
    NdalamaGoalsTheme {
        AddContributionScreen(
            onBack = {},
            onSave = {}
        )
    }
}