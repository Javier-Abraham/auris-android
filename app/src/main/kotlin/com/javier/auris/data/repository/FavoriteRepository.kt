package com.javier.auris.data.repository

import com.javier.auris.data.db.FavoriteDao
import com.javier.auris.data.db.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val dao: FavoriteDao) {

    fun getFavoriteIds(): Flow<List<Int>> = dao.getFavoriteIds()

    suspend fun add(soundId: Int) = dao.insert(FavoriteEntity(soundId))

    suspend fun remove(soundId: Int) = dao.delete(soundId)
}
