package com.darlingson.ndalamagoals.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darlingson.ndalamagoals.data.appViewModel
import com.darlingson.ndalamagoals.ui.theme.NdalamaGoalsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContributionScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    mainViewModel: appViewModel
) {
    var amount by remember { mutableStateOf("0") }
    var selectedCategory by remember { mutableStateOf("Savings") }
    var description by remember { mutableStateOf("") }
    var additionalNotes by remember { mutableStateOf("") }

    val categories = listOf("Savings", "Income", "Gift")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Contribution") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onSave,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
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
                    Text("$", fontSize = 48.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Light)
                    BasicTextField(
                        value = amount,
                        onValueChange = { newValue ->
                            // Allow only digits
                            if (newValue.all { it.isDigit() }) {
                                amount = newValue
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("USD", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 18.sp)
                }
            }

            // Contributing To Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
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
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Flight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("CONTRIBUTING TO", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                            Text("Japan Trip", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Category Chips
            item {
                Text("Category", color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Medium)
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
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurface
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
                    label = { Text("Date", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    leadingIcon = {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
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
                    label = { Text("Description", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    leadingIcon = {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    placeholder = { Text("Short description", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Additional Notes
            item {
                OutlinedTextField(
                    value = additionalNotes,
                    onValueChange = { additionalNotes = it },
                    label = { Text("Additional Notes", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    leadingIcon = {
                        Icon(Icons.Default.Note, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    placeholder = { Text("Add details about this contribution...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
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
            onSave = {},
            mainViewModel = mainViewModel
        )
    }
}
