package com.javier.auris.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mixes")
data class MixEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
)
