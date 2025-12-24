package com.darlingson.ndalamagoals.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GoalItem(icon: ImageVector, name: String, amount: String, target: String, due: String, progress: Float, status: String, onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp).clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                Text("$amount of $target", color = MaterialTheme.colorScheme.onSurfaceVariant)
                LinearProgressIndicator(progress = progress, color = if (status == "On Track") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
                Text(due, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(status, color = if (status == "On Track") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                Text("Update Balance", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
        }
    }
}