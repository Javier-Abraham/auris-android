package com.javier.auris.data

import com.javier.auris.R
import com.javier.auris.data.model.Sound
import com.javier.auris.data.model.SoundCategory

object SoundRepository {

    val sounds: List<Sound> = listOf(
        Sound(1, "Lluvia suave",  "El relajante sonido de la lluvia cayendo sobre el techo",    R.raw.lluvia,       SoundCategory.NATURALEZA, R.drawable.lluvia),
        Sound(2, "Océano",        "Olas del mar rompiendo suavemente en la orilla",             R.raw.oceano,       SoundCategory.NATURALEZA, R.drawable.oceano),
        Sound(3, "Bosque",        "Pájaros y brisa entre los árboles de un bosque tranquilo",  R.raw.bosque,       SoundCategory.NATURALEZA, R.drawable.bosque),
        Sound(4, "Tormenta",      "Lluvia intensa acompañada de truenos lejanos",               R.raw.tormenta,     SoundCategory.NATURALEZA, R.drawable.tormenta),
        Sound(5, "Chimenea",      "El crepitar de leños ardiendo en una chimenea acogedora",   R.raw.chimenea,     SoundCategory.AMBIENTE,   R.drawable.chimenea),
        Sound(6, "Viento",        "La suave brisa del viento entre hojas y ramas",             R.raw.viento,       SoundCategory.NATURALEZA, R.drawable.viento),
        Sound(7, "Ruido blanco",  "Sonido neutro y uniforme para bloquear distracciones",      R.raw.ruido_blanco, SoundCategory.RELAJACION, R.drawable.ruido_blanco),
        Sound(8, "Concentración", "Ambiente sonoro diseñado para mantener el foco mental",     R.raw.concentracion,SoundCategory.RELAJACION, R.drawable.concentracion),
    )
}
