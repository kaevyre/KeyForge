package ua.chnu.kmp.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/** Raw palette of the "Retro Keycap" design. */
object RetroPalette {
    val Rust = Color(0xFFC8461F)
    val RustLight = Color(0xFFDA5A31)
    val Mustard = Color(0xFFE9B23C)
    val Teal = Color(0xFF2F6F6A)
    val TealBright = Color(0xFF3A8A84)
    val Ink = Color(0xFF2B2622)
    val Cream = Color(0xFFF3EBDD)
    val Paper = Color(0xFFFBF6EC)
    val Beige = Color(0xFFE6DCC8)
    val Stone = Color(0xFF8C8478)
    val Muted = Color(0xFF6B6158)
    val Body = Color(0xFF4A423B)
    val Track = Color(0xFFD9CDB8)
    val White = Color(0xFFFFFFFF)

    val Night = Color(0xFF1E1A17)
    val NightSurface = Color(0xFF2B2622)
    val NightVariant = Color(0xFF3A332D)
    val NightOutline = Color(0xFF0E0C0A)
    val NightMuted = Color(0xFFB5AA9C)
    val NightBody = Color(0xFFD8CEBF)
}

/** Colors that Material's ColorScheme has no slot for. */
@Immutable
data class RetroColors(
    val outline: Color,
    val muted: Color,
    val body: Color,
    val rust: Color,
    val onRust: Color,
    val mustard: Color,
    val onMustard: Color,
    val teal: Color,
    val onTeal: Color,
    val keycap: Color,
    val onKeycap: Color,
    val modKey: Color,
    val onModKey: Color,
    val inverse: Color,
    val onInverse: Color,
    val track: Color,
    val stage: Color,
)

val LightRetroColors = RetroColors(
    outline = RetroPalette.Ink,
    muted = RetroPalette.Muted,
    body = RetroPalette.Body,
    rust = RetroPalette.Rust,
    onRust = RetroPalette.White,
    mustard = RetroPalette.Mustard,
    onMustard = RetroPalette.Ink,
    teal = RetroPalette.Teal,
    onTeal = RetroPalette.Cream,
    keycap = RetroPalette.Beige,
    onKeycap = RetroPalette.Ink,
    modKey = RetroPalette.Stone,
    onModKey = RetroPalette.Paper,
    inverse = RetroPalette.Ink,
    onInverse = RetroPalette.Cream,
    track = RetroPalette.Track,
    stage = RetroPalette.Beige,
)

val DarkRetroColors = RetroColors(
    outline = RetroPalette.NightOutline,
    muted = RetroPalette.NightMuted,
    body = RetroPalette.NightBody,
    rust = RetroPalette.RustLight,
    onRust = RetroPalette.White,
    mustard = RetroPalette.Mustard,
    onMustard = RetroPalette.Ink,
    teal = RetroPalette.TealBright,
    onTeal = RetroPalette.Cream,
    keycap = RetroPalette.Body,
    onKeycap = RetroPalette.Cream,
    modKey = RetroPalette.Stone,
    onModKey = RetroPalette.Paper,
    inverse = RetroPalette.Cream,
    onInverse = RetroPalette.Ink,
    track = RetroPalette.NightVariant,
    stage = RetroPalette.NightVariant,
)
