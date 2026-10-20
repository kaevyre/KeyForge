package ua.chnu.kmp.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** How a key is colored inside a [KeyboardColors] scheme. */
enum class KeyKind { Alpha, Mod, Accent, Highlight }

@Immutable
data class KeySpec(val label: String, val weight: Float = 1f, val kind: KeyKind = KeyKind.Alpha)

@Immutable
data class KeyboardColors(
    val shell: Color,
    val alpha: Color,
    val onAlpha: Color,
    val mod: Color,
    val onMod: Color,
    val accent: Color,
    val onAccent: Color,
    val highlight: Color = accent,
    val onHighlight: Color = onAccent,
)

/** A small drawn keyboard made of keycaps — used in onboarding and in the builder preview. */
@Composable
fun KeyboardPreview(
    rows: List<List<KeySpec>>,
    colors: KeyboardColors,
    modifier: Modifier = Modifier,
    keyHeight: Dp = 30.dp,
    keyRadius: Dp = 7.dp,
) {
    Keycap(
        modifier = modifier,
        color = colors.shell,
        shape = RoundedCornerShape(16.dp),
        depth = 6.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(9.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            rows.forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    row.forEach { key ->
                        val (bg, fg) = when (key.kind) {
                            KeyKind.Alpha -> colors.alpha to colors.onAlpha
                            KeyKind.Mod -> colors.mod to colors.onMod
                            KeyKind.Accent -> colors.accent to colors.onAccent
                            KeyKind.Highlight -> colors.highlight to colors.onHighlight
                        }
                        Keycap(
                            modifier = Modifier.weight(key.weight).height(keyHeight + 3.dp),
                            color = bg,
                            contentColor = fg,
                            shape = RoundedCornerShape(keyRadius),
                            depth = 3.dp,
                            borderWidth = 1.5.dp,
                        ) {
                            Text(
                                text = key.label,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, letterSpacing = 0.sp),
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Full Ukrainian ЙЦУКЕН layout, five rows. */
val OnboardingKeyboardRows: List<List<KeySpec>> = listOf(
    listOf(KeySpec("Esc", kind = KeyKind.Accent)) + "1234567".map { KeySpec(it.toString()) } + KeySpec("Del", 1.6f, KeyKind.Mod),
    listOf(KeySpec("Tab", 1.5f, KeyKind.Mod)) + "ЙЦУКЕНГ".map { KeySpec(it.toString()) },
    listOf(KeySpec("Caps", 1.8f, KeyKind.Mod)) + "ФІВАПР".map { KeySpec(it.toString()) } + KeySpec("Ent", 1.6f, KeyKind.Highlight),
    listOf(KeySpec("Shift", 2.3f, KeyKind.Mod)) + "ЯЧСМИ".map { KeySpec(it.toString()) } + KeySpec("↑"),
    listOf(KeySpec("Ctrl", 1.3f, KeyKind.Mod), KeySpec("", 5.5f, KeyKind.Highlight), KeySpec("Fn", 1.3f, KeyKind.Mod), KeySpec("→")),
)

/** Compact 4-row layout for the builder; the spacebar carries the engraving. */
fun builderKeyboardRows(spaceLabel: String): List<List<KeySpec>> = listOf(
    listOf(KeySpec("Esc", kind = KeyKind.Accent)) + "123456".map { KeySpec(it.toString()) } + KeySpec("Del", 1.6f, KeyKind.Accent),
    listOf(KeySpec("Tab", 1.5f, KeyKind.Accent)) + "ЙЦУКЕН".map { KeySpec(it.toString()) },
    listOf(KeySpec("Caps", 1.8f, KeyKind.Accent)) + "ФІВАП".map { KeySpec(it.toString()) } + KeySpec("Ent", 1.6f, KeyKind.Accent),
    listOf(KeySpec("Ctrl", 1.3f, KeyKind.Accent), KeySpec(spaceLabel, 5.5f, KeyKind.Accent), KeySpec("Fn", 1.3f, KeyKind.Accent)),
)
