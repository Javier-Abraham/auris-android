package com.javier.auris.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auris_settings")

class SettingsRepository(private val context: Context) {

    companion object {
        private val KEY_ACCENT  = intPreferencesKey("accent_color_index")
        private val KEY_VOLUME  = floatPreferencesKey("default_volume")
        private val KEY_TIMER   = intPreferencesKey("default_timer")
    }

    // 0 = Azul, 1 = Púrpura, 2 = Verde
    val accentColorIndex: Flow<Int> = context.dataStore.data.map { it[KEY_ACCENT] ?: 0 }

    // 0.0f – 1.0f
    val defaultVolume: Flow<Float> = context.dataStore.data.map { it[KEY_VOLUME] ?: 1f }

    // 0 = Sin límite, 15, 30, 60
    val defaultTimer: Flow<Int> = context.dataStore.data.map { it[KEY_TIMER] ?: 0 }

    suspend fun setAccentColor(index: Int)    = context.dataStore.edit { it[KEY_ACCENT] = index }
    suspend fun setDefaultVolume(value: Float) = context.dataStore.edit { it[KEY_VOLUME] = value }
    suspend fun setDefaultTimer(minutes: Int)  = context.dataStore.edit { it[KEY_TIMER]  = minutes }
}
