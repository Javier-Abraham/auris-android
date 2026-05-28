package com.javier.auris.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.javier.auris.AurisApp
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as AurisApp).settingsRepository

    val accentColorIndex = repo.accentColorIndex
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val defaultVolume = repo.defaultVolume
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1f)

    val defaultTimer = repo.defaultTimer
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setAccentColor(index: Int)    = viewModelScope.launch { repo.setAccentColor(index) }
    fun setDefaultVolume(v: Float)    = viewModelScope.launch { repo.setDefaultVolume(v) }
    fun setDefaultTimer(minutes: Int) = viewModelScope.launch { repo.setDefaultTimer(minutes) }
}
