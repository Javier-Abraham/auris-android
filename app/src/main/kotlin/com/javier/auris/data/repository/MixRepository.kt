package com.javier.auris.data.repository

import com.javier.auris.data.db.MixDao
import com.javier.auris.data.db.MixEntity
import com.javier.auris.data.db.MixSoundCrossRef
import com.javier.auris.data.db.MixWithSoundIds
import kotlinx.coroutines.flow.Flow

class MixRepository(private val dao: MixDao) {

    fun getMixes(): Flow<List<MixWithSoundIds>> = dao.getMixesWithSoundIds()

    suspend fun createMix(name: String, soundIds: List<Int>) {
        val mixId = dao.insertMix(MixEntity(name = name)).toInt()
        if (soundIds.isNotEmpty()) {
            dao.insertCrossRefs(soundIds.map { MixSoundCrossRef(mixId, it) })
        }
    }

    suspend fun updateMix(mixId: Int, name: String, soundIds: List<Int>) {
        dao.updateMixName(mixId, name)
        dao.deleteCrossRefsForMix(mixId)
        if (soundIds.isNotEmpty()) {
            dao.insertCrossRefs(soundIds.map { MixSoundCrossRef(mixId, it) })
        }
    }

    suspend fun deleteMix(mixId: Int) {
        dao.deleteCrossRefsForMix(mixId)
        dao.deleteMixById(mixId)
    }
}
