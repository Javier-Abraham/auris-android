package com.javier.auris.viewmodel

import android.app.Application
import android.content.ComponentName
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.javier.auris.AurisApp
import com.javier.auris.data.SoundRepository
import com.javier.auris.data.model.Sound
import com.javier.auris.data.model.SoundCategory
import com.javier.auris.service.AurisPlaybackService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val pkg          = application.packageName
    private val favoriteRepo = (application as AurisApp).favoriteRepository
    private val settingsRepo = (application as AurisApp).settingsRepository

    // ── MediaController (async – connects to AurisPlaybackService) ────────────
    private var controller: MediaController? = null
    private var pendingAction: (() -> Unit)? = null

    private val controllerFuture = MediaController
        .Builder(application, SessionToken(application, ComponentName(application, AurisPlaybackService::class.java)))
        .buildAsync()

    // ── Sounds – combines catalog with Room favorites ──────────────────────────
    val sounds: StateFlow<List<Sound>> = favoriteRepo.getFavoriteIds()
        .map { favIds ->
            val favSet = favIds.toSet()
            SoundRepository.sounds.map { it.copy(isFavorite = it.id in favSet) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SoundRepository.sounds)

    // currentSoundId + sounds derive currentSound so isFavorite is always fresh
    private val _currentSoundId = MutableStateFlow<Int?>(null)
    val currentSound: StateFlow<Sound?> = combine(_currentSoundId, sounds) { id, list ->
        list.find { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isPlaying    = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _volume = MutableStateFlow(1f)
    val volume: StateFlow<Float> = _volume

    private val _timerMinutes = MutableStateFlow(0)
    val timerMinutes: StateFlow<Int> = _timerMinutes

    private val _timerSecondsRemaining = MutableStateFlow(0L)
    val timerSecondsRemaining: StateFlow<Long> = _timerSecondsRemaining

    private val _selectedCategory = MutableStateFlow<SoundCategory?>(null)
    val selectedCategory: StateFlow<SoundCategory?> = _selectedCategory

    private var timerJob: Job? = null

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(playing: Boolean) { _isPlaying.value = playing }
        override fun onPlayerError(error: PlaybackException) {
            Log.w("PlayerVM", "Playback error: ${error.message}")
            _isPlaying.value = false
        }
    }

    init {
        controllerFuture.addListener({
            try {
                val ctrl = controllerFuture.get()
                ctrl.addListener(playerListener)
                controller = ctrl
                // Apply saved default volume
                viewModelScope.launch {
                    val vol = settingsRepo.defaultVolume.first()
                    _volume.value = vol
                    ctrl.volume = vol
                }
                // Execute any command queued before controller was ready
                pendingAction?.invoke()
                pendingAction = null
            } catch (e: Exception) {
                Log.e("PlayerVM", "MediaController connection failed", e)
            }
        }, ContextCompat.getMainExecutor(application))
    }

    // ── Playback ──────────────────────────────────────────────────────────────

    fun selectAndPlay(sound: Sound) {
        _currentSoundId.value = sound.id
        val ctrl = controller
        if (ctrl == null) { pendingAction = { doPlay(sound) }; return }
        doPlay(sound)
        applyDefaultTimer()
    }

    fun playMix(sounds: List<Sound>) {
        val playable = sounds.filter { it.rawResId != null }
        if (playable.isEmpty()) return
        _currentSoundId.value = playable.first().id
        val ctrl = controller ?: return
        val items = playable.map { s ->
            MediaItem.fromUri(Uri.parse("android.resource://$pkg/${s.rawResId}"))
        }
        ctrl.setMediaItems(items)
        ctrl.repeatMode = Player.REPEAT_MODE_ALL
        ctrl.prepare()
        ctrl.play()
    }

    fun togglePlayPause() {
        val ctrl = controller ?: return
        if (ctrl.isPlaying) ctrl.pause() else ctrl.play()
    }

    fun playNext() {
        val list = filteredList()
        if (list.isEmpty()) return
        val idx = list.indexOfFirst { it.id == _currentSoundId.value }
        selectAndPlay(list[(idx + 1) % list.size])
    }

    fun playPrevious() {
        val list = filteredList()
        if (list.isEmpty()) return
        val idx = list.indexOfFirst { it.id == _currentSoundId.value }
        selectAndPlay(list[if (idx <= 0) list.lastIndex else idx - 1])
    }

    // ── Volume & Timer ────────────────────────────────────────────────────────

    fun setVolume(value: Float) {
        _volume.value = value
        controller?.volume = value
    }

    fun cycleTimer() {
        val next = when (_timerMinutes.value) { 0 -> 15; 15 -> 30; 30 -> 60; else -> 0 }
        applyTimer(next)
    }

    // ── Favorites ─────────────────────────────────────────────────────────────

    fun toggleFavorite(soundId: Int) {
        viewModelScope.launch {
            val isFav = sounds.value.find { it.id == soundId }?.isFavorite ?: false
            if (isFav) favoriteRepo.remove(soundId) else favoriteRepo.add(soundId)
        }
    }

    fun setCategory(category: SoundCategory?) { _selectedCategory.value = category }

    // ── Private helpers ───────────────────────────────────────────────────────

    private fun doPlay(sound: Sound) {
        val rawResId = sound.rawResId ?: return
        val ctrl = controller ?: return
        ctrl.setMediaItem(MediaItem.fromUri(Uri.parse("android.resource://$pkg/$rawResId")))
        ctrl.repeatMode = Player.REPEAT_MODE_ONE
        ctrl.prepare()
        ctrl.play()
    }

    private fun applyDefaultTimer() {
        viewModelScope.launch {
            val minutes = settingsRepo.defaultTimer.first()
            if (minutes > 0) applyTimer(minutes)
        }
    }

    private fun applyTimer(minutes: Int) {
        timerJob?.cancel()
        _timerMinutes.value = minutes
        if (minutes > 0) {
            _timerSecondsRemaining.value = minutes * 60L
            timerJob = viewModelScope.launch {
                while (_timerSecondsRemaining.value > 0) {
                    delay(1000)
                    _timerSecondsRemaining.value -= 1
                }
                controller?.pause()
                _timerMinutes.value = 0
            }
        } else {
            _timerSecondsRemaining.value = 0
        }
    }

    private fun filteredList(): List<Sound> {
        val cat = _selectedCategory.value ?: return sounds.value
        return sounds.value.filter { it.category == cat }
    }

    override fun onCleared() {
        timerJob?.cancel()
        MediaController.releaseFuture(controllerFuture)
        super.onCleared()
    }
}
