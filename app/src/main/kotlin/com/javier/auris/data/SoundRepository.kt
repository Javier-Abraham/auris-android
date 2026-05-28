package com.javier.auris.data

import com.javier.auris.R
import com.javier.auris.data.model.Sound
import com.javier.auris.data.model.SoundCategory

object SoundRepository {

    val sounds: List<Sound> = listOf(
        Sound(1, "Lluvia suave",  "El relajante sonido de la lluvia cayendo sobre el techo",   SoundCategory.NATURALEZA, R.drawable.lluvia,        rawResId = R.raw.lluvia),
        Sound(2, "Océano",        "Olas del mar rompiendo suavemente en la orilla",            SoundCategory.NATURALEZA, R.drawable.oceano,        rawResId = R.raw.oceano),
        Sound(3, "Bosque",        "Pájaros y brisa entre los árboles de un bosque tranquilo", SoundCategory.NATURALEZA, R.drawable.bosque,        rawResId = R.raw.bosque),
        Sound(4, "Tormenta",      "Lluvia intensa acompañada de truenos lejanos",              SoundCategory.NATURALEZA, R.drawable.tormenta,      rawResId = R.raw.tormenta),
        Sound(5, "Chimenea",      "El crepitar de leños ardiendo en una chimenea acogedora",  SoundCategory.AMBIENTE,   R.drawable.chimenea,      rawResId = R.raw.chimenea),
        Sound(6, "Viento",        "La suave brisa del viento entre hojas y ramas",            SoundCategory.NATURALEZA, R.drawable.viento,        rawResId = R.raw.viento),
        Sound(7, "Ruido blanco",  "Sonido neutro y uniforme para bloquear distracciones",     SoundCategory.RELAJACION, R.drawable.ruido_blanco,  rawResId = R.raw.ruido_blanco),
        Sound(8, "Concentración", "Ambiente sonoro diseñado para mantener el foco mental",    SoundCategory.RELAJACION, R.drawable.concentracion, rawResId = R.raw.concentracion),
    )
}
