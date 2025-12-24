package com.darlingson.ndalamagoals.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NdalamaGreen,
    background = DarkBackground,
    surface = DarkTopBar, // Using the TopAppBar color for the main surface
    onPrimary = Color.Black,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurface, // Using the Card color for surface variants
    onSurfaceVariant = DarkOnSurfaceVariant,
    error = StatusRed
)

private val LightColorScheme = lightColorScheme(
    primary = NdalamaGreen,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = Color.Black,
    onBackground = LightOnSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurface,
    onSurfaceVariant = LightOnSurfaceVariant,
    error = StatusRed
)

@Composable
fun NdalamaGoalsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar color to match the top app bar
            window.statusBarColor = colorScheme.surface.toArgb()
            // Set the status bar icons to be light or dark
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
