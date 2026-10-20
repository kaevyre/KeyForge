package ua.chnu.kmp.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

private val LightScheme = lightColorScheme(
    primary = RetroPalette.Rust,
    onPrimary = RetroPalette.White,
    secondary = RetroPalette.Mustard,
    onSecondary = RetroPalette.Ink,
    tertiary = RetroPalette.Teal,
    onTertiary = RetroPalette.Cream,
    background = RetroPalette.Cream,
    onBackground = RetroPalette.Ink,
    surface = RetroPalette.Paper,
    onSurface = RetroPalette.Ink,
    surfaceVariant = RetroPalette.Beige,
    onSurfaceVariant = RetroPalette.Muted,
    outline = RetroPalette.Ink,
    inverseSurface = RetroPalette.Ink,
    inverseOnSurface = RetroPalette.Cream,
)

private val DarkScheme = darkColorScheme(
    primary = RetroPalette.RustLight,
    onPrimary = RetroPalette.White,
    secondary = RetroPalette.Mustard,
    onSecondary = RetroPalette.Ink,
    tertiary = RetroPalette.TealBright,
    onTertiary = RetroPalette.Cream,
    background = RetroPalette.Night,
    onBackground = RetroPalette.Cream,
    surface = RetroPalette.NightSurface,
    onSurface = RetroPalette.Cream,
    surfaceVariant = RetroPalette.NightVariant,
    onSurfaceVariant = RetroPalette.NightMuted,
    outline = RetroPalette.NightOutline,
    inverseSurface = RetroPalette.Cream,
    inverseOnSurface = RetroPalette.Ink,
)

private val RetroShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

private val LocalRetroColors = staticCompositionLocalOf { LightRetroColors }

/** Custom KeyForge theme: retro keycap colors, Rubik / Rubik Mono One / IBM Plex Mono. */
@Composable
fun KeyForgeTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalRetroColors provides if (darkTheme) DarkRetroColors else LightRetroColors) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkScheme else LightScheme,
            typography = retroTypography(),
            shapes = RetroShapes,
            content = content,
        )
    }
}

object RetroTheme {
    val colors: RetroColors
        @Composable
        @ReadOnlyComposable
        get() = LocalRetroColors.current
}
