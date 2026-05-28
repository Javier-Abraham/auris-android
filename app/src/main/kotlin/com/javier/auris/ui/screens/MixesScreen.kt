package com.javier.auris.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javier.auris.data.SoundRepository
import com.javier.auris.ui.theme.Accent
import com.javier.auris.ui.theme.Background
import com.javier.auris.ui.theme.Card
import com.javier.auris.ui.theme.Surface
import com.javier.auris.ui.theme.TextPrimary
import com.javier.auris.ui.theme.TextSecondary
import com.javier.auris.viewmodel.MixUiState
import com.javier.auris.viewmodel.MixesViewModel
import com.javier.auris.viewmodel.PlayerViewModel

@Composable
fun MixesScreen(
    mixesViewModel: MixesViewModel,
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
) {
    val mixes by mixesViewModel.mixes.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editingMix by remember { mutableStateOf<MixUiState?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        if (mixes.isEmpty()) {
            EmptyMixes(Modifier.fillMaxSize())
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(mixes, key = { it.id }) { mix ->
                    MixCard(
                        mix       = mix,
                        onPlay    = { playerViewModel.playMix(mix.sounds) },
                        onEdit    = { editingMix = mix; showDialog = true },
                        onDelete  = { mixesViewModel.deleteMix(mix.id) },
                    )
                }
            }
        }

        FloatingActionButton(
            onClick            = { editingMix = null; showDialog = true },
            modifier           = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor     = Accent,
            contentColor       = Color.White,
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Nueva mezcla")
        }
    }

    if (showDialog) {
        MixDialog(
            initial   = editingMix,
            onDismiss = { showDialog = false; editingMix = null },
            onConfirm = { name, soundIds ->
                val editing = editingMix
                if (editing == null) mixesViewModel.createMix(name, soundIds)
                else mixesViewModel.updateMix(editing.id, name, soundIds)
                showDialog = false
                editingMix = null
            },
        )
    }
}

// ── Mix Card ─────────────────────────────────────────────────────────────────

@Composable
private fun MixCard(
    mix: MixUiState,
    onPlay: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Card, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(mix.name, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(2.dp))
            val label = if (mix.sounds.isEmpty()) "Sin sonidos"
                        else mix.sounds.joinToString(" · ") { it.name }
            Text(
                text     = label,
                color    = TextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
            )
        }
        Spacer(Modifier.width(8.dp))
        IconButton(onClick = onPlay) {
            Icon(Icons.Rounded.PlayArrow, null, tint = Accent, modifier = Modifier.size(26.dp))
        }
        IconButton(onClick = onEdit) {
            Icon(Icons.Rounded.Edit, null, tint = TextSecondary, modifier = Modifier.size(22.dp))
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Rounded.Delete, null, tint = Color(0xFFEF5350), modifier = Modifier.size(22.dp))
        }
    }
}

// ── Create / Edit Dialog ──────────────────────────────────────────────────────

@Composable
private fun MixDialog(
    initial: MixUiState?,
    onDismiss: () -> Unit,
    onConfirm: (name: String, soundIds: List<Int>) -> Unit,
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    val selectedIds = remember {
        mutableStateListOf<Int>().also { list ->
            initial?.sounds?.forEach { list.add(it.id) }
        }
    }
    val allSounds = SoundRepository.sounds

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = Surface,
        title = {
            Text(
                text       = if (initial == null) "Nueva mezcla" else "Editar mezcla",
                color      = TextPrimary,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                OutlinedTextField(
                    value         = name,
                    onValueChange = { name = it },
                    label         = { Text("Nombre", color = TextSecondary) },
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = Accent,
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.5f),
                        focusedTextColor     = TextPrimary,
                        unfocusedTextColor   = TextPrimary,
                        cursorColor          = Accent,
                    ),
                )
                Spacer(Modifier.height(16.dp))
                Text("Sonidos:", color = TextSecondary, fontSize = 12.sp)
                Spacer(Modifier.height(6.dp))
                allSounds.forEach { sound ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier          = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                    ) {
                        Checkbox(
                            checked         = sound.id in selectedIds,
                            onCheckedChange = { checked ->
                                if (checked) selectedIds.add(sound.id)
                                else selectedIds.remove(sound.id)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor   = Accent,
                                uncheckedColor = TextSecondary,
                            ),
                        )
                        Text(sound.name, color = TextPrimary, fontSize = 14.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) onConfirm(name.trim(), selectedIds.toList())
                },
            ) {
                Text(
                    text  = if (initial == null) "Crear" else "Guardar",
                    color = Accent,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = TextSecondary) }
        },
    )
}

// ── Empty state ───────────────────────────────────────────────────────────────

@Composable
private fun EmptyMixes(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector        = Icons.Rounded.LibraryMusic,
                contentDescription = null,
                tint               = TextSecondary,
                modifier           = Modifier.size(64.dp),
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text       = "Sin mezclas",
                color      = TextPrimary,
                fontSize   = 18.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text     = "Toca + para crear tu primera mezcla",
                color    = TextSecondary,
                fontSize = 13.sp,
            )
        }
    }
}
