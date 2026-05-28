package com.javier.auris.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.javier.auris.data.model.Sound
import com.javier.auris.data.model.SoundCategory
import com.javier.auris.ui.components.SoundCard
import com.javier.auris.ui.theme.Accent
import com.javier.auris.ui.theme.Background
import com.javier.auris.ui.theme.Blue
import com.javier.auris.ui.theme.BlueBright
import com.javier.auris.ui.theme.Card
import com.javier.auris.ui.theme.Surface
import com.javier.auris.ui.theme.TextPrimary
import com.javier.auris.ui.theme.TextSecondary
import com.javier.auris.ui.utils.categoryDisplayName
import com.javier.auris.ui.utils.soundIcon
import com.javier.auris.viewmodel.FavoritesViewModel
import com.javier.auris.viewmodel.MixesViewModel
import com.javier.auris.viewmodel.PlayerViewModel
import com.javier.auris.viewmodel.SettingsViewModel

private data class BottomNavItem(val label: String, val icon: ImageVector)

private val navItems = listOf(
    BottomNavItem("Inicio",    Icons.Rounded.Home),
    BottomNavItem("Favoritos", Icons.Rounded.Favorite),
    BottomNavItem("Mezclas",   Icons.Rounded.Tune),
    BottomNavItem("Ajustes",   Icons.Rounded.Settings),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PlayerViewModel = viewModel(),
    onNavigateToPlayer: () -> Unit,
) {
    val favoritesViewModel: FavoritesViewModel = viewModel()
    val mixesViewModel: MixesViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()

    val sounds       by viewModel.sounds.collectAsState()
    val currentSound by viewModel.currentSound.collectAsState()
    val isPlaying    by viewModel.isPlaying.collectAsState()
    val selectedCat  by viewModel.selectedCategory.collectAsState()
    var selectedTab  by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text          = "AURIS",
                        fontSize      = 22.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = TextPrimary,
                        letterSpacing = 4.sp,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
            )
        },
        bottomBar = {
            Column {
                AnimatedVisibility(
                    visible = currentSound != null,
                    enter   = slideInVertically { it } + fadeIn(),
                    exit    = slideOutVertically { it } + fadeOut(),
                ) {
                    currentSound?.let { sound ->
                        MiniPlayer(
                            sound        = sound,
                            isPlaying    = isPlaying,
                            onTogglePlay = viewModel::togglePlayPause,
                            onTap        = onNavigateToPlayer,
                        )
                    }
                }
                NavigationBar(containerColor = Surface) {
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick  = { selectedTab = index },
                            icon     = { Icon(item.icon, item.label) },
                            label    = { Text(item.label, fontSize = 11.sp) },
                            colors   = NavigationBarItemDefaults.colors(
                                selectedIconColor   = Accent,
                                selectedTextColor   = Accent,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor      = Background,
                            ),
                        )
                    }
                }
            }
        },
        containerColor = Background,
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (selectedTab) {
                0 -> SoundsTabContent(
                    sounds           = sounds,
                    currentSound     = currentSound,
                    isPlaying        = isPlaying,
                    selectedCat      = selectedCat,
                    onSetCategory    = viewModel::setCategory,
                    onTogglePlay     = { s -> if (currentSound?.id == s.id) viewModel.togglePlayPause() else viewModel.selectAndPlay(s) },
                    onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                    onNavigate       = { s -> if (currentSound?.id != s.id) viewModel.selectAndPlay(s); onNavigateToPlayer() },
                )
                1 -> FavoritesScreen(
                    favoritesViewModel = favoritesViewModel,
                    playerViewModel    = viewModel,
                    onNavigateToPlayer = onNavigateToPlayer,
                )
                2 -> MixesScreen(
                    mixesViewModel = mixesViewModel,
                    playerViewModel = viewModel,
                )
                3 -> SettingsScreen(viewModel = settingsViewModel)
            }
        }
    }
}

// ── Inicio tab ────────────────────────────────────────────────────────────────

@Composable
private fun SoundsTabContent(
    sounds: List<Sound>,
    currentSound: Sound?,
    isPlaying: Boolean,
    selectedCat: SoundCategory?,
    onSetCategory: (SoundCategory?) -> Unit,
    onTogglePlay: (Sound) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onNavigate: (Sound) -> Unit,
) {
    val displayed = remember(sounds, selectedCat) {
        if (selectedCat == null) sounds else sounds.filter { it.category == selectedCat }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            contentPadding        = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                FilterChip(
                    selected = selectedCat == null,
                    onClick  = { onSetCategory(null) },
                    label    = { Text("Todos") },
                    colors   = aurisChipColors(),
                )
            }
            items(SoundCategory.entries) { cat ->
                FilterChip(
                    selected = selectedCat == cat,
                    onClick  = { onSetCategory(cat) },
                    label    = { Text(categoryDisplayName(cat)) },
                    colors   = aurisChipColors(),
                )
            }
        }

        LazyVerticalGrid(
            columns               = GridCells.Fixed(2),
            contentPadding        = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement   = Arrangement.spacedBy(12.dp),
            modifier              = Modifier.fillMaxSize(),
        ) {
            items(displayed, key = { it.id }) { sound ->
                val isCurrent = currentSound?.id == sound.id
                SoundCard(
                    sound               = sound,
                    isCurrentAndPlaying = isCurrent && isPlaying,
                    isCurrent           = isCurrent,
                    onTogglePlay        = { onTogglePlay(sound) },
                    onToggleFavorite    = { onToggleFavorite(sound.id) },
                    onClick             = { onNavigate(sound) },
                )
            }
        }
    }
}

// ── Mini Player ───────────────────────────────────────────────────────────────

@Composable
private fun MiniPlayer(
    sound: Sound,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onTap: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Card)
            .clickable(onClick = onTap)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model              = sound.imageRes,
                contentDescription = null,
                modifier           = Modifier.fillMaxSize(),
                contentScale       = ContentScale.Crop,
            )
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.35f)))
            Icon(soundIcon(sound.id), null, tint = Color.White, modifier = Modifier.size(22.dp))
        }

        Column(Modifier.weight(1f)) {
            Text(sound.name, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text     = if (isPlaying) "Reproduciendo" else "En pausa",
                color    = TextSecondary,
                fontSize = 11.sp,
            )
        }

        IconButton(onClick = onTogglePlay) {
            Icon(
                imageVector        = if (isPlaying) Icons.Rounded.PauseCircle else Icons.Rounded.PlayCircle,
                contentDescription = null,
                tint               = BlueBright,
                modifier           = Modifier.size(38.dp),
            )
        }
    }
}

@Composable
private fun aurisChipColors() = FilterChipDefaults.filterChipColors(
    containerColor         = Card,
    labelColor             = TextSecondary,
    selectedContainerColor = Blue,
    selectedLabelColor     = Color.White,
)
