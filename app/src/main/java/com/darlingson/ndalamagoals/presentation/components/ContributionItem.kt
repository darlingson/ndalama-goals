package com.darlingson.ndalamagoals.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

