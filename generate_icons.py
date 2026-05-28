#!/usr/bin/env python3
"""
generate_icons.py — AURIS launcher icon generator
===================================================
Renderiza el ícono oficial de AURIS (luna creciente + waveform)
y exporta PNG en todas las densidades Android + store icon 512x512.

Uso:
    pip install Pillow
    python generate_icons.py

Salida:
    app/src/main/res/mipmap-mdpi/ic_launcher.png         (48x48)
    app/src/main/res/mipmap-hdpi/ic_launcher.png         (72x72)
    app/src/main/res/mipmap-xhdpi/ic_launcher.png        (96x96)
    app/src/main/res/mipmap-xxhdpi/ic_launcher.png       (144x144)
    app/src/main/res/mipmap-xxxhdpi/ic_launcher.png      (192x192)
    store_icon_512x512.png                               (512x512)
"""

import math
import os
import sys

try:
    from PIL import Image, ImageDraw, ImageFilter
except ImportError:
    sys.exit(
        "ERROR: Pillow no instalado.\n"
        "Ejecutá: pip install Pillow\n"
    )

# ── Paleta ────────────────────────────────────────────────────────────────────
BG_COLOR   = (0x0D, 0x1B, 0x2A, 255)   # #0D1B2A  fondo
MOON_COLOR = (0xC8, 0xD8, 0xE8, 255)   # #C8D8E8  luna iluminada
WAVE_COLOR = (0x7E, 0xC8, 0xE3, 255)   # #7EC8E3  waveform

# ── Diseño en coordenadas viewport (0–108) ───────────────────────────────────
# Safe zone: 18dp de margen → zona útil (18,18)→(90,90)
VP = 108.0

# Luna creciente
MOON_CX, MOON_CY, MOON_R    = 46.0, 38.0, 17.0   # círculo iluminado
SHADOW_CX, SHADOW_CY, SHAD_R = 53.0, 35.0, 15.0  # círculo sombra (fondo)

# Waveform
WAVE_BASELINE = 65.0   # y del centro de la onda
WAVE_X0       = 22.0   # inicio horizontal
WAVE_X1       = 82.0   # fin horizontal
WAVE_CYCLES   = 2.5    # ciclos completos a lo largo del eje X


def vp(value: float, scale: float) -> float:
    """Convierte coordenada viewport a píxeles."""
    return value * scale


def draw_filled_circle(draw: ImageDraw.ImageDraw, cx, cy, r, color, scale):
    """Dibuja un círculo sólido en coordenadas viewport."""
    x0 = vp(cx - r, scale)
    y0 = vp(cy - r, scale)
    x1 = vp(cx + r, scale)
    y1 = vp(cy + r, scale)
    draw.ellipse([x0, y0, x1, y1], fill=color)


def waveform_points(scale: float) -> list[tuple[float, float]]:
    """
    Genera los puntos del waveform sinusoidal con envolvente en campana.
    La amplitud es máxima en el centro y decrece hacia los extremos,
    simulando la envolvente de una onda de audio real.
    """
    pts = []
    n_steps = 400  # suavidad

    for i in range(n_steps + 1):
        t = i / n_steps  # 0.0 → 1.0

        # Posición X
        x_vp = WAVE_X0 + t * (WAVE_X1 - WAVE_X0)

        # Envolvente Gaussiana: amplitud máx = 8dp, mín ≈ 3dp en bordes
        sigma = 0.28
        amplitude = 3.0 + 7.0 * math.exp(-((t - 0.5) ** 2) / (2 * sigma ** 2))

        # Onda sinusoidal
        angle = t * WAVE_CYCLES * 2 * math.pi
        y_vp = WAVE_BASELINE + amplitude * math.sin(angle)

        pts.append((vp(x_vp, scale), vp(y_vp, scale)))

    return pts


def create_icon(size: int) -> Image.Image:
    """Crea el ícono AURIS a `size` × `size` píxeles."""
    # Renderizamos a 4× para anti-aliasing, luego reducimos
    ss = 4
    render_size = size * ss
    scale = render_size / VP

    img = Image.new("RGBA", (render_size, render_size), BG_COLOR)
    draw = ImageDraw.Draw(img)

    # ── Luna: círculo iluminado ───────────────────────────────────────────
    draw_filled_circle(draw, MOON_CX, MOON_CY, MOON_R, MOON_COLOR, scale)

    # ── Luna: círculo sombra (recorta para crear el creciente) ────────────
    draw_filled_circle(draw, SHADOW_CX, SHADOW_CY, SHAD_R, BG_COLOR, scale)

    # ── Waveform ──────────────────────────────────────────────────────────
    pts = waveform_points(scale)
    stroke_px = max(ss, round(3.2 * scale))  # mínimo 1px en destino
    draw.line(pts, fill=WAVE_COLOR, width=stroke_px, joint="curve")

    # ── Reducción con anti-aliasing ───────────────────────────────────────
    img = img.resize((size, size), Image.LANCZOS)
    return img


def create_rounded_icon(size: int) -> Image.Image:
    """
    Versión del ícono con esquinas redondeadas para el store icon
    (Google Play requiere PNG cuadrado; el sistema aplica la máscara).
    """
    img = create_icon(size)
    # Máscara circular suave
    mask = Image.new("L", (size, size), 0)
    mask_draw = ImageDraw.Draw(mask)
    radius = int(size * 0.22)  # squircle aprox.
    mask_draw.rounded_rectangle([0, 0, size - 1, size - 1], radius=radius, fill=255)
    result = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    result.paste(img, mask=mask)
    return result


# ── Configuración de densidades ───────────────────────────────────────────────
DENSITIES = [
    ("mdpi",     48),
    ("hdpi",     72),
    ("xhdpi",    96),
    ("xxhdpi",  144),
    ("xxxhdpi", 192),
]


def main():
    base = os.path.dirname(os.path.abspath(__file__))
    res  = os.path.join(base, "app", "src", "main", "res")

    print("Generando íconos AURIS...\n")

    # ── Mipmap PNGs ───────────────────────────────────────────────────────
    for density, size in DENSITIES:
        folder = os.path.join(res, f"mipmap-{density}")
        os.makedirs(folder, exist_ok=True)

        img = create_icon(size)

        for name in ("ic_launcher.png", "ic_launcher_round.png"):
            path = os.path.join(folder, name)
            img.save(path, "PNG")

        print(f"  OK mipmap-{density:<10} {size:>3}x{size:<3}  ->  {folder}")

    # ── Store icon 512×512 ────────────────────────────────────────────────
    store_path = os.path.join(base, "store_icon_512x512.png")
    store = create_rounded_icon(512)
    store.save(store_path, "PNG")
    print(f"\n  OK store_icon_512x512.png (esquinas redondeadas)  ->  {store_path}")

    print("\nListo. Compilá el proyecto para ver el ícono en el dispositivo.")


if __name__ == "__main__":
    main()
