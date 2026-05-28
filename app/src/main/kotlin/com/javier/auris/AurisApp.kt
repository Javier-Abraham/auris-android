package com.javier.auris

import android.app.Application
import com.javier.auris.data.db.AurisDatabase
import com.javier.auris.data.repository.FavoriteRepository
import com.javier.auris.data.repository.MixRepository

class AurisApp : Application() {
    val database by lazy { AurisDatabase.getInstance(this) }
    val favoriteRepository by lazy { FavoriteRepository(database.favoriteDao()) }
    val mixRepository by lazy { MixRepository(database.mixDao()) }
}
