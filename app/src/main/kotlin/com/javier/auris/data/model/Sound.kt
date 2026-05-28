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
    val category: SoundCategory,
    @DrawableRes val imageRes: Int,
    @RawRes val rawResId: Int? = null,   // null mientras no haya audio real
    var isFavorite: Boolean = false,
)
