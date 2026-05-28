package com.javier.auris.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AurisDarkColorScheme = darkColorScheme(
    primary          = BlueBright,
    onPrimary        = Background,
    primaryContainer = Blue,
    secondary        = Accent,
    onSecondary      = Background,
    background       = Background,
    onBackground     = TextPrimary,
    surface          = Surface,
    onSurface        = TextPrimary,
    surfaceVariant   = Card,
    onSurfaceVariant = TextSecondary,
)

@Composable
fun AurisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AurisDarkColorScheme,
        content     = content,
    )
}
