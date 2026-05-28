package com.javier.auris.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.javier.auris.AurisApp
import com.javier.auris.data.SoundRepository
import com.javier.auris.data.model.Sound
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as AurisApp).favoriteRepository

    val favorites: StateFlow<List<Sound>> = repo.getFavoriteIds()
        .map { favIds ->
            val favSet = favIds.toSet()
            SoundRepository.sounds
                .filter { it.id in favSet }
                .map { it.copy(isFavorite = true) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
