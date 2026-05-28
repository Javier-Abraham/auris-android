package com.javier.auris.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

enum class SoundCategory {
    NATURALEZA,
    AMBIENTE,
    RELAJACION,
}

data class Sound(
    val id: Int,
    val name: String,
    val description: String,
    @RawRes val rawResId: Int,
    val category: SoundCategory,
    @DrawableRes val imageRes: Int? = null,
    var isFavorite: Boolean = false,
)
