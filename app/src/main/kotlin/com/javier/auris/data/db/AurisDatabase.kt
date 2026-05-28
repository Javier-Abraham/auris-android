package com.javier.auris.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteEntity::class, MixEntity::class, MixSoundCrossRef::class],
    version = 1,
    exportSchema = false,
)
abstract class AurisDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
    abstract fun mixDao(): MixDao

    companion object {
        @Volatile private var INSTANCE: AurisDatabase? = null

        fun getInstance(context: Context): AurisDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room
                    .databaseBuilder(context, AurisDatabase::class.java, "auris.db")
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
