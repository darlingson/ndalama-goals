package com.darlingson.ndalamagoals.data

import java.text.DecimalFormat
import kotlin.math.absoluteValue

fun formatAmount(
    amount: Double,
    pattern: String = "#,##0.00"
): String {
    val abs = amount.absoluteValue
    if (abs < 10_000_000) {
        return DecimalFormat(pattern).format(amount)
    }

    val (divisor, suffix) = when {
        abs < 1_000_000_000 -> 1_000_000.0 to "M"
        else                -> 1_000_000_000.0 to "B"
    }
    val core = DecimalFormat("0.##").format(amount / divisor)
    return "$core $suffix"
}