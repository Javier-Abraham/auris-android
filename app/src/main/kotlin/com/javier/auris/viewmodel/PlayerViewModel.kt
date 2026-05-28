package com.javier.auris.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.javier.auris.data.SoundRepository
import com.javier.auris.data.model.Sound
import com.javier.auris.data.model.SoundCategory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val player = ExoPlayer.Builder(application).build().apply {
        repeatMode = Player.REPEAT_MODE_ONE
    }

    private val _sounds = MutableStateFlow(SoundRepository.sounds)
    val sounds: StateFlow<List<Sound>> = _sounds

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
        })
    }

    fun selectAndPlay(sound: Sound) {
        val uri = Uri.parse(
            "android.resource://${getApplication<Application>().packageName}/${sound.rawResId}"
        )
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
        player.play()
        _currentSound.value = sound
    }

    fun togglePlayPause() {
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
        val updated = _sounds.value.map { s ->
            if (s.id == soundId) s.copy(isFavorite = !s.isFavorite) else s
        }
        _sounds.value = updated
        if (_currentSound.value?.id == soundId) {
            _currentSound.value = updated.find { it.id == soundId }
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
        val cat = _selectedCategory.value ?: return _sounds.value
        return _sounds.value.filter { it.category == cat }
    }

    override fun onCleared() {
        timerJob?.cancel()
        player.release()
        super.onCleared()
    }
}
