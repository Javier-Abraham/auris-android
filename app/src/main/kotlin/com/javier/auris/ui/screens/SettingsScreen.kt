package com.javier.auris.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javier.auris.ui.theme.Card
import com.javier.auris.ui.theme.TextPrimary
import com.javier.auris.ui.theme.TextSecondary
import com.javier.auris.ui.theme.accentColors
import com.javier.auris.ui.theme.accentLabels
import com.javier.auris.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
) {
    val accentIndex   by viewModel.accentColorIndex.collectAsState()
    val defaultVolume by viewModel.defaultVolume.collectAsState()
    val defaultTimer  by viewModel.defaultTimer.collectAsState()

    val accent = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        // ── APARIENCIA ────────────────────────────────────────────────────────
        SectionHeader("APARIENCIA")

        SettingsCard {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Color de acento", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    accentColors.forEachIndexed { index, color ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .then(
                                        if (accentIndex == index)
                                            Modifier.border(3.dp, Color.White, CircleShape)
                                        else Modifier
                                    )
                                    .clickable { viewModel.setAccentColor(index) },
                                contentAlignment = Alignment.Center,
                            ) {
                                if (accentIndex == index) {
                                    Icon(Icons.Rounded.Check, null, tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(accentLabels[index], color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // ── REPRODUCCIÓN ──────────────────────────────────────────────────────
        SectionHeader("REPRODUCCIÓN")

        SettingsCard {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Volumen por defecto", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    Text(
                        text  = "${(defaultVolume * 100).toInt()}%",
                        color = accent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Slider(
                    value         = defaultVolume,
                    onValueChange = { viewModel.setDefaultVolume(it) },
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = SliderDefaults.colors(
                        thumbColor         = accent,
                        activeTrackColor   = accent,
                        inactiveTrackColor = TextSecondary.copy(alpha = 0.3f),
                    ),
                )
            }
        }

        SettingsCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Temporizador por defecto", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                val options = listOf(0 to "Sin límite", 15 to "15 min", 30 to "30 min", 60 to "60 min")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEach { (minutes, label) ->
                        val selected = defaultTimer == minutes
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (selected) accent else TextSecondary.copy(alpha = 0.15f))
                                .clickable { viewModel.setDefaultTimer(minutes) }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text       = label,
                                color      = if (selected) Color.White else TextSecondary,
                                fontSize   = 12.sp,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            )
                        }
                    }
                }
            }
        }

        // ── INFORMACIÓN ───────────────────────────────────────────────────────
        SectionHeader("INFORMACIÓN")

        SettingsCard {
            InfoItem(
                icon  = Icons.Rounded.Info,
                title = "Acerca de AURIS",
                value = "v1.0.0",
            )
        }

        SettingsCard {
            InfoItem(
                icon  = Icons.Rounded.HelpOutline,
                title = "Ayuda y soporte",
                subtitle = "javierjoseabraham@gmail.com",
                showArrow = true,
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

// ── Components ────────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String) {
    Text(
        text     = title,
        color    = TextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.5.sp,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 2.dp),
    )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Card)
            .padding(16.dp),
    ) { content() }
}

@Composable
private fun InfoItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    value: String? = null,
    showArrow: Boolean = false,
) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            if (subtitle != null) Text(subtitle, color = TextSecondary, fontSize = 12.sp)
        }
        if (value != null) Text(value, color = TextSecondary, fontSize = 13.sp)
        if (showArrow) Icon(Icons.Rounded.ChevronRight, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
    }
}
