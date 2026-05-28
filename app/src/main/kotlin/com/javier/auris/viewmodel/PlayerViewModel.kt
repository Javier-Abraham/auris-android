package com.javier.auris.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.javier.auris.AurisApp
import com.javier.auris.data.SoundRepository
import com.javier.auris.data.model.Sound
import com.javier.auris.data.model.SoundCategory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteRepo = (application as AurisApp).favoriteRepository

    private val player = ExoPlayer.Builder(application).build().apply {
        repeatMode = Player.REPEAT_MODE_ONE
    }

    // Combines in-memory catalog with Room favorites so isFavorite is always persisted
    val sounds: StateFlow<List<Sound>> = favoriteRepo.getFavoriteIds()
        .map { favIds ->
            val favSet = favIds.toSet()
            SoundRepository.sounds.map { it.copy(isFavorite = it.id in favSet) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SoundRepository.sounds)

    private val _currentSound = MutableStateFlow<Sound?>(null)
    val currentSound: StateFlow<Sound?> = _currentSound

    private val _isPlaying = MutableStateFlow(false)
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

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }
            override fun onPlayerError(error: PlaybackException) {
                Log.w("PlayerViewModel", "Playback error: ${error.message}")
                _isPlaying.value = false
            }
        })
    }

    fun selectAndPlay(sound: Sound) {
        _currentSound.value = sound
        val rawResId = sound.rawResId ?: return
        val uri = Uri.parse("android.resource://${getApplication<Application>().packageName}/$rawResId")
        player.setMediaItem(MediaItem.fromUri(uri))
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.prepare()
        player.play()
    }

    fun playMix(sounds: List<Sound>) {
        val playable = sounds.filter { it.rawResId != null }
        if (playable.isEmpty()) return
        val pkg = getApplication<Application>().packageName
        val items = playable.map { s ->
            MediaItem.fromUri(Uri.parse("android.resource://$pkg/${s.rawResId}"))
        }
        player.setMediaItems(items)
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.prepare()
        player.play()
        _currentSound.value = playable.first()
    }

    fun togglePlayPause() {
        if (_currentSound.value?.rawResId == null) return
        if (player.isPlaying) player.pause() else player.play()
    }

    fun playNext() {
        val list = filteredList()
        if (list.isEmpty()) return
        val idx = list.indexOfFirst { it.id == _currentSound.value?.id }
        selectAndPlay(list[(idx + 1) % list.size])
    }

    fun playPrevious() {
        val list = filteredList()
        if (list.isEmpty()) return
        val idx = list.indexOfFirst { it.id == _currentSound.value?.id }
        selectAndPlay(list[if (idx <= 0) list.lastIndex else idx - 1])
    }

    fun setVolume(value: Float) {
        _volume.value = value
        player.volume = value
    }

    fun cycleTimer() {
        val next = when (_timerMinutes.value) {
            0    -> 15
            15   -> 30
            30   -> 60
            else -> 0
        }
        applyTimer(next)
    }

    fun toggleFavorite(soundId: Int) {
        viewModelScope.launch {
            val isFav = sounds.value.find { it.id == soundId }?.isFavorite ?: false
            if (isFav) favoriteRepo.remove(soundId) else favoriteRepo.add(soundId)
        }
    }

    fun setCategory(category: SoundCategory?) {
        _selectedCategory.value = category
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
                player.pause()
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
        player.release()
        super.onCleared()
    }
}
