package com.javier.auris.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.javier.auris.data.model.Sound
import com.javier.auris.ui.theme.BlueBright
import com.javier.auris.ui.utils.categoryDisplayName
import com.javier.auris.ui.utils.soundIcon

private val FavoriteActive = Color(0xFFFF6B8A)

@Composable
fun SoundCard(
    sound: Sound,
    isCurrentAndPlaying: Boolean,
    isCurrent: Boolean,
    onTogglePlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderMod = if (isCurrent)
        Modifier.border(2.dp, BlueBright.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
    else Modifier

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .then(borderMod)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model              = sound.imageRes,
            contentDescription = null,
            modifier           = Modifier.fillMaxSize(),
            contentScale       = ContentScale.Crop,
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.40f)))

        Column(
            modifier            = Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Top row: sound icon (left) + heart (right)
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Top,
            ) {
                Icon(
                    imageVector        = soundIcon(sound.id),
                    contentDescription = null,
                    tint               = Color.White.copy(alpha = 0.9f),
                    modifier           = Modifier.size(40.dp),
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.30f), CircleShape)
                        .clickable(onClick = onToggleFavorite),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector        = if (sound.isFavorite) Icons.Rounded.Favorite
                                             else Icons.Rounded.FavoriteBorder,
                        contentDescription = null,
                        tint               = if (sound.isFavorite) FavoriteActive
                                             else Color.White.copy(alpha = 0.8f),
                        modifier           = Modifier.size(18.dp),
                    )
                }
            }

            // Bottom: name + category + play button
            Column {
                Text(
                    text       = sound.name,
                    color      = Color.White,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis,
                )
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    Text(
                        text     = categoryDisplayName(sound.category),
                        color    = Color.White.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White.copy(alpha = 0.25f), CircleShape)
                            .clickable(onClick = onTogglePlay),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector        = if (isCurrentAndPlaying) Icons.Rounded.Pause
                                                 else Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            tint               = Color.White,
                            modifier           = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }
    }
}
