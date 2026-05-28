package com.javier.auris.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

fun aurisColorScheme(accent: Color) = darkColorScheme(
    primary          = accent,
    onPrimary        = Background,
    primaryContainer = accent.copy(alpha = 0.3f),
    secondary        = accent.copy(alpha = 0.8f),
    onSecondary      = Background,
    background       = Background,
    onBackground     = TextPrimary,
    surface          = Surface,
    onSurface        = TextPrimary,
    surfaceVariant   = Card,
    onSurfaceVariant = TextSecondary,
)

@Composable
fun AurisTheme(
    accentColorIndex: Int = 0,
    content: @Composable () -> Unit,
) {
    val accent = accentColors.getOrElse(accentColorIndex) { AccentBlue }
    MaterialTheme(
        colorScheme = aurisColorScheme(accent),
        typography  = AurisTypography,
        content     = content,
    )
}
