# AURIS — Ficha de Google Play Store
> Listo para copiar y pegar en Play Console. Última revisión: 28 mayo 2026.

---

## TÍTULO (máx 30 caracteres — actual: 27)

```
AURIS - Sonidos Ambientales
```

---

## DESCRIPCIÓN CORTA (máx 80 caracteres — actual: 51)

```
Lluvia, bosque y océano para dormir y concentrarte
```

---

## DESCRIPCIÓN LARGA (máx 4000 caracteres)

```
🌧️ AURIS — Tu refugio de sonido para cada momento del día

¿Tenés problemas para dormir? ¿Necesitás concentrarte en el trabajo o el estudio? ¿Buscás un momento de calma en medio del ruido urbano? AURIS es la app de sonidos ambientales que necesitás.

🎵 8 SONIDOS AMBIENTALES DE ALTA CALIDAD
Cada sonido fue seleccionado y optimizado para acompañarte en diferentes momentos:

🌧️ Lluvia suave — el relajante sonido de la lluvia cayendo sobre el techo
🌊 Océano — olas del mar rompiendo suavemente en la orilla
🌿 Bosque — pájaros y brisa entre los árboles de un bosque tranquilo
⛈️ Tormenta — lluvia intensa acompañada de truenos lejanos
🔥 Chimenea — el crepitar de leños ardiendo en una chimenea acogedora
💨 Viento — la suave brisa del viento entre hojas y ramas
📊 Ruido blanco — sonido neutro y uniforme para bloquear distracciones
🎯 Concentración — ambiente sonoro diseñado para mantener el foco mental

✨ FUNCIONES PRINCIPALES

🎛️ Mezclas personalizadas
Combiná tus sonidos favoritos en mezclas únicas. Guardá combinaciones como "Lluvia + Chimenea" para tus sesiones de estudio o "Océano + Viento" para relajarte antes de dormir.

❤️ Favoritos
Marcá con corazón los sonidos que más usás y accedé a ellos al instante desde la pestaña Favoritos.

⏱️ Temporizador de apagado
Configurá un temporizador de 15, 30 o 60 minutos para que la app se detenga automáticamente mientras dormís. No más preocupaciones por batería.

🔔 Notificación persistente
Controlá la reproducción directamente desde la barra de notificaciones sin necesidad de abrir la app. Pausá, retomá y cambiá sonidos desde cualquier pantalla.

🎨 Personalización
Elegí el color de acento que más te guste (azul, púrpura o verde), ajustá el volumen por defecto y configurá el temporizador predeterminado para cada sesión.

🌙 CASOS DE USO

• 😴 DORMIR — El ruido blanco y la lluvia suave enmascaran el ruido ambiental y ayudan al cerebro a entrar en modo sueño.
• 📚 ESTUDIAR — Los sonidos de naturaleza y concentración mejoran el foco y reducen la distracción.
• 🧘 MEDITAR — El océano y el bosque crean un espacio mental tranquilo ideal para la meditación.
• 💼 TRABAJAR — El sonido de chimenea o café crea un ambiente productivo tipo "coffee shop".
• 🌙 RELAJARSE — Después de un día largo, unos minutos de sonidos ambientales calman el sistema nervioso.
• 👶 BEBÉS — El ruido blanco es uno de los métodos más efectivos para calmar y ayudar a dormir a bebés.

🔒 PRIVACIDAD TOTAL
AURIS no recopila ningún dato personal. No requiere cuenta ni registro. Los sonidos están incluidos en la app y funcionan sin conexión a internet. Todos tus favoritos y mezclas se guardan únicamente en tu dispositivo.

💚 GRATIS Y SIN ANUNCIOS
AURIS es completamente gratuita y no tiene anuncios, compras in-app ni suscripciones. Tu experiencia no se interrumpe.

AURIS fue desarrollada con amor usando Jetpack Compose, Media3/ExoPlayer y Material Design 3.
```

---

## CATEGORÍA

```
Salud y bienestar
```

## SUBCATEGORÍA

```
Meditación y mindfulness
```

---

## CLASIFICACIÓN DE CONTENIDO

```
PEGI 3 / Everyone (E)
— Sin violencia
— Sin lenguaje adulto
— Sin compras in-app
— Apto para todas las edades
```

---

## PALABRAS CLAVE / TAGS (máx 5 en Play Console)

```
sonidos ambientales
ruido blanco
dormir
meditación
relajación
```

**Adicionales para ASO (usar en descripción y título):**
lluvia, océano, bosque, concentración, sleep sounds, white noise, nature sounds, ambient

---

## CAPTURAS DE PANTALLA RECOMENDADAS
> Tamaño requerido: mínimo 320px, máximo 3840px por lado. Relación 9:16.
> Formato: PNG o JPEG. Subir en este orden.

| # | Pantalla | Qué mostrar |
|---|---|---|
| 1 | **HomeScreen — Grid de sonidos** | Grid 2×4 con las 8 tarjetas de sonido, imágenes de naturaleza, en el tema oscuro. El MiniPlayer visible abajo reproduciendo "Lluvia suave". |
| 2 | **PlayerScreen — Lluvia** | Pantalla completa con imagen de lluvia de fondo, nombre grande "Lluvia suave", slider de volumen y botones de control. Timer visible. |
| 3 | **FavoritesScreen** | Tab "Favoritos" activa con 3-4 sonidos marcados con corazón. Estado lleno (no vacío). |
| 4 | **MixesScreen — crear mezcla** | Dialog de creación abierto con nombre "Noche de estudio" y checkboxes de Lluvia + Ruido blanco seleccionados. |
| 5 | **SettingsScreen** | Pantalla de ajustes completa: selector de color de acento (púrpura seleccionado), slider de volumen al 80%, selector de temporizador en 30 min. |
| 6 | **Notificación persistente** | Mostrar la notificación expandida en la barra de notificaciones con controles play/pause y el nombre del sonido. |

**Especificaciones técnicas:**
- Dispositivo de captura: Samsung Galaxy S20 FE (SM-G781B)
- Resolución nativa: 2400×1080 → exportar a 1080×2400 px (portrait)
- Usar `adb shell screencap -p /sdcard/screen.png && adb pull /sdcard/screen.png`

---

## FEATURE GRAPHIC (1024×500 px)
> Imagen de cabecera que aparece en Play Store al inicio.

**Concepto:**
- **Fondo:** gradiente vertical de `#0A0E1A` (arriba) a `#0D2040` (abajo), con textura de ruido muy sutil.
- **Centro-izquierda:** luna creciente grande en `#C8D8E8` con un suave halo/glow azulado.
- **Centro:** waveform sinusoidal de 5 ondas en `#7EC8E3` con alpha degradado en los extremos.
- **Centro-derecha:** texto `AURIS` en blanco, fuente Poppins SemiBold, tracking amplio (letter-spacing 8px), tamaño 72px.
- **Subtítulo bajo AURIS:** "Sonidos ambientales" en `#7EC8E3`, Poppins Regular, 24px.
- **Estrellas/partículas:** 5-8 puntos blancos pequeños dispersos en la mitad superior, simulando un cielo nocturno.
- **Sin bordes ni marcos.** El diseño respira con márgenes de al menos 60px por lado.
- **Herramienta sugerida:** Canva, Figma o Photoshop. Exportar JPG calidad 90+.

---

## DATOS DE CONTACTO

```
Email de desarrollador: javierjoseabraham@gmail.com
Sitio web: https://javier-abraham.github.io/auris-android/
```

---

## POLÍTICA DE PRIVACIDAD

```
URL: https://javier-abraham.github.io/auris-android/privacy-policy
```

---

## CHECKLIST ANTES DE PUBLICAR

- [ ] AAB firmado: `app/build/outputs/bundle/release/app-release.aab`
- [ ] versionCode = 1 / versionName = "1.0" en build.gradle.kts
- [ ] applicationId = "com.javier.auris" (único en Play Store)
- [ ] Ícono subido: `store_icon_512x512.png` (512×512 PNG sin transparencia)
- [ ] Feature graphic creado (1024×500 JPG)
- [ ] 6 capturas de pantalla subidas
- [ ] Política de privacidad con URL válida
- [ ] Formulario de clasificación de contenido completado en Play Console
- [ ] Precio: Gratis
- [ ] Países: Todos los disponibles (o seleccionar)
- [ ] Revisión interna completada antes de enviar a producción
