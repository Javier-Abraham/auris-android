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
import kotlinx.coroutines.launch

data class MixUiState(
    val id: Int,
    val name: String,
    val sounds: List<Sound>,
)

class MixesViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as AurisApp).mixRepository

    val mixes: StateFlow<List<MixUiState>> = repo.getMixes()
        .map { list ->
            list.map { mws ->
                MixUiState(
                    id     = mws.mix.id,
                    name   = mws.mix.name,
                    sounds = SoundRepository.sounds.filter { it.id in mws.soundIds },
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createMix(name: String, soundIds: List<Int>) {
        viewModelScope.launch { repo.createMix(name, soundIds) }
    }

    fun updateMix(mixId: Int, name: String, soundIds: List<Int>) {
        viewModelScope.launch { repo.updateMix(mixId, name, soundIds) }
    }

    fun deleteMix(mixId: Int) {
        viewModelScope.launch { repo.deleteMix(mixId) }
    }
}
