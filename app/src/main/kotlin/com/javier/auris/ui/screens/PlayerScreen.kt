package com.javier.auris.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.VolumeDown
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.javier.auris.ui.theme.Accent
import com.javier.auris.ui.theme.BlueBright
import com.javier.auris.ui.theme.TextPrimary
import com.javier.auris.ui.theme.TextSecondary
import com.javier.auris.ui.utils.categoryDisplayName
import com.javier.auris.ui.utils.categoryGradient
import com.javier.auris.ui.utils.soundIcon
import com.javier.auris.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = viewModel(),
    onBack: () -> Unit,
) {
    val currentSound          by viewModel.currentSound.collectAsState()
    val isPlaying             by viewModel.isPlaying.collectAsState()
    val volume                by viewModel.volume.collectAsState()
    val timerMinutes          by viewModel.timerMinutes.collectAsState()
    val timerSecondsRemaining by viewModel.timerSecondsRemaining.collectAsState()

    val sound = currentSound ?: run { onBack(); return }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Background: photo or gradient ──────────────────────────────
        if (sound.imageRes != null) {
            Image(
                painter            = painterResource(sound.imageRes),
                contentDescription = null,
                modifier           = Modifier.fillMaxSize(),
                contentScale       = ContentScale.Crop,
            )
        } else {
            Box(modifier = Modifier.fillMaxSize().background(categoryGradient(sound.category)))
        }
        // Dark overlay
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.52f)))

        // ── UI ─────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
        ) {
            // Top bar
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Rounded.ArrowBackIosNew, "Volver", tint = TextPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("REPRODUCIENDO", color = TextSecondary, fontSize = 10.sp, letterSpacing = 2.sp)
                    Text(categoryDisplayName(sound.category), color = Accent, fontSize = 12.sp)
                }
                IconButton(onClick = { viewModel.toggleFavorite(sound.id) }) {
                    Icon(
                        imageVector        = if (sound.isFavorite) Icons.Rounded.Favorite
                                             else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint               = if (sound.isFavorite) Color(0xFFFF6B6B) else TextPrimary,
                    )
                }
            }

            // Main content
            Column(
                modifier              = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment   = Alignment.CenterHorizontally,
                verticalArrangement   = Arrangement.SpaceEvenly,
            ) {
                // Large icon circle
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color.White.copy(alpha = 0.12f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector        = soundIcon(sound.id),
                        contentDescription = null,
                        tint               = Color.White,
                        modifier           = Modifier.size(88.dp),
                    )
                }

                // Name & description
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text       = sound.name,
                        fontSize   = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary,
                        textAlign  = TextAlign.Center,
                    )
                    Text(
                        text       = sound.description,
                        fontSize   = 14.sp,
                        color      = TextSecondary,
                        textAlign  = TextAlign.Center,
                        lineHeight = 22.sp,
                    )
                }

                // Volume
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Volumen", color = TextSecondary, fontSize = 12.sp)
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(Icons.Rounded.VolumeDown, null, tint = TextSecondary,
                            modifier = Modifier.size(22.dp))
                        Slider(
                            value         = volume,
                            onValueChange = viewModel::setVolume,
                            modifier      = Modifier.weight(1f),
                            colors        = SliderDefaults.colors(
                                thumbColor         = BlueBright,
                                activeTrackColor   = BlueBright,
                                inactiveTrackColor = Color.White.copy(alpha = 0.2f),
                            ),
                        )
                        Icon(Icons.Rounded.VolumeUp, null, tint = TextSecondary,
                            modifier = Modifier.size(22.dp))
                    }
                }

                // Playback controls + timer
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                ) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment     = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = viewModel::playPrevious, modifier = Modifier.size(56.dp)) {
                            Icon(Icons.Rounded.SkipPrevious, "Anterior",
                                tint = TextPrimary, modifier = Modifier.size(40.dp))
                        }
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(BlueBright, CircleShape)
                                .clickable(onClick = viewModel::togglePlayPause),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector        = if (isPlaying) Icons.Rounded.Pause
                                                     else Icons.Rounded.PlayArrow,
                                contentDescription = null,
                                tint               = Color.White,
                                modifier           = Modifier.size(40.dp),
                            )
                        }
                        IconButton(onClick = viewModel::playNext, modifier = Modifier.size(56.dp)) {
                            Icon(Icons.Rounded.SkipNext, "Siguiente",
                                tint = TextPrimary, modifier = Modifier.size(40.dp))
                        }
                    }

                    // Timer
                    Row(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                            .clickable(onClick = viewModel::cycleTimer)
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            Icons.Rounded.Timer, null,
                            tint     = if (timerMinutes > 0) Accent else TextSecondary,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text       = timerLabel(timerMinutes, timerSecondsRemaining),
                            color      = if (timerMinutes > 0) Accent else TextSecondary,
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}

private fun timerLabel(minutes: Int, secondsRemaining: Long): String {
    if (minutes == 0) return "Sin límite"
    if (secondsRemaining > 0) {
        val m = secondsRemaining / 60
        val s = secondsRemaining % 60
        return "%d:%02d".format(m, s)
    }
    return "$minutes min"
}
