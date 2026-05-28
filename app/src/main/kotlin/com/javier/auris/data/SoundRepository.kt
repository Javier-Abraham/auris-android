package com.javier.auris.data

import com.javier.auris.R
import com.javier.auris.data.model.Sound
import com.javier.auris.data.model.SoundCategory

object SoundRepository {

    val sounds: List<Sound> = listOf(
        Sound(1, "Lluvia suave", "El relajante sonido de la lluvia cayendo sobre el techo",
            rawResId  = R.raw.lluvia,
            category  = SoundCategory.NATURALEZA,
            imageRes  = R.drawable.lluvia),
        Sound(2, "Océano", "Olas del mar rompiendo suavemente en la orilla",
            rawResId  = R.raw.oceano,
            category  = SoundCategory.NATURALEZA,
            imageRes  = R.drawable.oceano),
        Sound(3, "Bosque", "Pájaros y brisa entre los árboles de un bosque tranquilo",
            rawResId  = R.raw.bosque,
            category  = SoundCategory.NATURALEZA,
            imageRes  = R.drawable.bosque),
        Sound(4, "Tormenta", "Lluvia intensa acompañada de truenos lejanos",
            rawResId  = R.raw.tormenta,
            category  = SoundCategory.NATURALEZA,
            imageRes  = R.drawable.tormenta),
        Sound(5, "Chimenea", "El crepitar de leños ardiendo en una chimenea acogedora",
            rawResId  = R.raw.chimenea,
            category  = SoundCategory.AMBIENTE,
            imageRes  = R.drawable.chimenea),
        Sound(6, "Viento", "La suave brisa del viento entre hojas y ramas",
            rawResId  = R.raw.viento,
            category  = SoundCategory.NATURALEZA),
        Sound(7, "Ruido blanco", "Sonido neutro y uniforme para bloquear distracciones",
            rawResId  = R.raw.ruido_blanco,
            category  = SoundCategory.RELAJACION),
        Sound(8, "Concentración", "Ambiente sonoro diseñado para mantener el foco mental",
            rawResId  = R.raw.concentracion,
            category  = SoundCategory.RELAJACION),
    )
}
