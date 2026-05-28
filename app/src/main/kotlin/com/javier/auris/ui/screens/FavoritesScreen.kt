package com.javier.auris.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javier.auris.data.model.Sound
import com.javier.auris.ui.components.SoundCard
import com.javier.auris.ui.theme.TextPrimary
import com.javier.auris.ui.theme.TextSecondary
import com.javier.auris.viewmodel.FavoritesViewModel
import com.javier.auris.viewmodel.PlayerViewModel

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel,
    playerViewModel: PlayerViewModel,
    onNavigateToPlayer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val favorites    by favoritesViewModel.favorites.collectAsState()
    val currentSound by playerViewModel.currentSound.collectAsState()
    val isPlaying    by playerViewModel.isPlaying.collectAsState()

    if (favorites.isEmpty()) {
        EmptyFavorites(modifier.fillMaxSize())
    } else {
        LazyVerticalGrid(
            columns               = GridCells.Fixed(2),
            contentPadding        = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement   = Arrangement.spacedBy(12.dp),
            modifier              = modifier.fillMaxSize(),
        ) {
            items(favorites, key = { it.id }) { sound ->
                val isCurrent = currentSound?.id == sound.id
                SoundCard(
                    sound               = sound,
                    isCurrentAndPlaying = isCurrent && isPlaying,
                    isCurrent           = isCurrent,
                    onTogglePlay        = {
                        if (isCurrent) playerViewModel.togglePlayPause()
                        else playerViewModel.selectAndPlay(sound)
                    },
                    onToggleFavorite    = { playerViewModel.toggleFavorite(sound.id) },
                    onClick             = {
                        if (!isCurrent) playerViewModel.selectAndPlay(sound)
                        onNavigateToPlayer()
                    },
                )
            }
        }
    }
}

@Composable
private fun EmptyFavorites(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector        = Icons.Rounded.FavoriteBorder,
                contentDescription = null,
                tint               = TextSecondary,
                modifier           = Modifier.size(64.dp),
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text       = "Sin favoritos",
                color      = TextPrimary,
                fontSize   = 18.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text     = "Toca el corazón en un sonido para guardarlo",
                color    = TextSecondary,
                fontSize = 13.sp,
            )
        }
    }
}
