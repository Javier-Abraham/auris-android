package com.javier.auris.data.db

import androidx.room.Entity

@Entity(
    tableName = "mix_sound_cross_ref",
    primaryKeys = ["mixId", "soundId"],
)
data class MixSoundCrossRef(
    val mixId: Int,
    val soundId: Int,
)
