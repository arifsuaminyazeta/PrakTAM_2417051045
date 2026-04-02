package com.example.praktam_2417051045.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    background = Background,
    surface = CardColor
)

@Composable
fun PraktiktamTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}