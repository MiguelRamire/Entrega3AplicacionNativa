package com.example.petshield.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PetShieldColors = lightColorScheme(
    primary = Color(0xFF146B63),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFDA744D),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFFDFB95F),
    background = Color(0xFFF5F7F2),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF202B28),
    surfaceVariant = Color(0xFFE8EEE8),
    onSurfaceVariant = Color(0xFF52615B),
    error = Color(0xFFB3261E)
)

@Composable
fun PetShieldTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PetShieldColors,
        content = content
    )
}