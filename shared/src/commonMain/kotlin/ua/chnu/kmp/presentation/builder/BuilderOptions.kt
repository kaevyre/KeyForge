package ua.chnu.kmp.presentation.builder

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import ua.chnu.kmp.presentation.theme.RetroPalette
import ua.chnu.kmp.presentation.ui.components.KeyboardColors
import ua.chnu.kmp.resources.Res
import ua.chnu.kmp.resources.switch_clicky
import ua.chnu.kmp.resources.switch_linear
import ua.chnu.kmp.resources.switch_tactile

const val BASE_PRICE_UAH = 4899
const val ENGRAVING_PRICE_UAH = 250

@Immutable
data class Colorway(
    val name: String,
    val shell: Color,
    val base: Color,
    val onBase: Color,
    val accent: Color,
    val onAccent: Color,
) {
    fun keyboardColors() = KeyboardColors(
        shell = shell,
        alpha = base,
        onAlpha = onBase,
        mod = accent,
        onMod = onAccent,
        accent = accent,
        onAccent = onAccent,
    )
}

val Colorways = listOf(
    Colorway("Rust & Cream", RetroPalette.Rust, RetroPalette.Beige, RetroPalette.Ink, RetroPalette.Mustard, RetroPalette.Ink),
    Colorway("Mustard", RetroPalette.Mustard, RetroPalette.Paper, RetroPalette.Ink, RetroPalette.Rust, RetroPalette.White),
    Colorway("Teal Terminal", RetroPalette.Teal, RetroPalette.Beige, RetroPalette.Ink, RetroPalette.Ink, RetroPalette.Mustard),
    Colorway("Beige 1986", RetroPalette.Beige, RetroPalette.Paper, RetroPalette.Ink, RetroPalette.Stone, RetroPalette.Paper),
    Colorway("Ink", RetroPalette.Ink, RetroPalette.Stone, RetroPalette.Paper, RetroPalette.Rust, RetroPalette.White),
)

enum class SwitchType(val title: String, val hint: StringResource, val surchargeUah: Int) {
    LINEAR("Linear", Res.string.switch_linear, 0),
    TACTILE("Tactile", Res.string.switch_tactile, 300),
    CLICKY("Clicky", Res.string.switch_clicky, 300),
}

enum class KeycapProfile(val title: String, val keyRadius: Dp, val surchargeUah: Int) {
    CHERRY("Cherry", 6.dp, 0),
    SA("SA", 10.dp, 500),
    XDA("XDA", 4.dp, 350),
}
