package com.javier.auris.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class MixWithSoundIds(
    @Embedded val mix: MixEntity,
    @Relation(
        entity = MixSoundCrossRef::class,
        parentColumn = "id",
        entityColumn = "mixId",
    )
    val crossRefs: List<MixSoundCrossRef>,
) {
    val soundIds: List<Int> get() = crossRefs.map { it.soundId }
}
