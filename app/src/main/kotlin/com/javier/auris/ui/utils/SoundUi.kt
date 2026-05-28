package com.javier.auris.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Park
import androidx.compose.material.icons.rounded.Sailing
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.javier.auris.data.model.SoundCategory

fun soundIcon(soundId: Int): ImageVector = when (soundId) {
    1 -> Icons.Rounded.WaterDrop
    2 -> Icons.Rounded.Sailing
    3 -> Icons.Rounded.Park
    4 -> Icons.Rounded.Bolt
    5 -> Icons.Rounded.LocalFireDepartment
    6 -> Icons.Rounded.Air
    7 -> Icons.Rounded.GraphicEq
    8 -> Icons.Rounded.LocalCafe
    else -> Icons.Rounded.MusicNote
}

fun categoryGradient(category: SoundCategory): Brush = when (category) {
    SoundCategory.NATURALEZA -> Brush.verticalGradient(listOf(Color(0xFF1B4332), Color(0xFF081F12)))
    SoundCategory.AMBIENTE   -> Brush.verticalGradient(listOf(Color(0xFF7C2D12), Color(0xFF3A0F05)))
    SoundCategory.RELAJACION -> Brush.verticalGradient(listOf(Color(0xFF312E81), Color(0xFF11103A)))
}

fun categoryDisplayName(category: SoundCategory): String = when (category) {
    SoundCategory.NATURALEZA -> "Naturaleza"
    SoundCategory.AMBIENTE   -> "Ambiente"
    SoundCategory.RELAJACION -> "Relajación"
}
