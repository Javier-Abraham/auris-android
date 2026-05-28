package com.javier.auris.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MixDao {

    @Transaction
    @Query("SELECT * FROM mixes ORDER BY createdAt DESC")
    fun getMixesWithSoundIds(): Flow<List<MixWithSoundIds>>

    @Insert
    suspend fun insertMix(mix: MixEntity): Long

    @Query("UPDATE mixes SET name = :name WHERE id = :mixId")
    suspend fun updateMixName(mixId: Int, name: String)

    @Query("DELETE FROM mixes WHERE id = :mixId")
    suspend fun deleteMixById(mixId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefs(crossRefs: List<MixSoundCrossRef>)

    @Query("DELETE FROM mix_sound_cross_ref WHERE mixId = :mixId")
    suspend fun deleteCrossRefsForMix(mixId: Int)
}
