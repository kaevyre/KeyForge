package ua.chnu.kmp.presentation.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ua.chnu.kmp.presentation.theme.RetroTheme

/**
 * The signature element of the retro design: a surface with a thick ink outline and a hard
 * shadow under it, like a keycap. When pressed (or selected) it travels down onto its shadow.
 *
 * With [onClick] == null it is a static surface.
 */
@Composable
fun Keycap(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    selected: Boolean = false,
    shape: Shape = RoundedCornerShape(14.dp),
    depth: Dp = 5.dp,
    borderWidth: Dp = 2.dp,
    role: Role = Role.Button,
    onClickLabel: String? = null,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val pushedDown = selected || (pressed && onClick != null)
    val travel by animateDpAsState(if (pushedDown) depth - 1.dp else 0.dp, label = "keycapTravel")
    val outline = RetroTheme.colors.outline

    val interaction = when {
        onClick == null -> Modifier
        role == Role.RadioButton || role == Role.Tab -> Modifier.selectable(
            selected = selected,
            interactionSource = interactionSource,
            indication = null,
            role = role,
            onClick = onClick,
        )
        else -> Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            role = role,
            onClickLabel = onClickLabel,
            onClick = onClick,
        )
    }

    Box(
        modifier = modifier
            .padding(bottom = depth)
            .offset { IntOffset(0, travel.roundToPx()) }
            .drawBehind {
                val shadow = shape.createOutline(size, layoutDirection, this)
                translate(top = (depth - travel).toPx()) { drawOutline(shadow, outline) }
            }
            .clip(shape)
            .background(color)
            .border(borderWidth, outline, shape)
            .then(interaction),
        contentAlignment = contentAlignment,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

/** Visual-only toggle in keycap style; make the whole row `toggleable` instead of this. */
@Composable
fun RetroSwitch(checked: Boolean, modifier: Modifier = Modifier) {
    val colors = RetroTheme.colors
    val knobOffset by animateDpAsState(if (checked) 24.dp else 0.dp, label = "switchKnob")
    val trackShape = RoundedCornerShape(9.dp)
    val knobShape = RoundedCornerShape(6.dp)
    Box(
        modifier = modifier
            .clip(trackShape)
            .background(if (checked) colors.teal else colors.track)
            .border(2.dp, colors.outline, trackShape)
            .padding(4.dp),
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(knobOffset.roundToPx(), 0) }
                .clip(knobShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(2.dp, colors.outline, knobShape)
                .padding(10.dp),
        )
    }
}

fun Modifier.dashedBorder(color: Color, width: Dp, cornerRadius: Dp): Modifier = drawBehind {
    val strokePx = width.toPx()
    val inset = strokePx / 2
    drawRoundRect(
        color = color,
        topLeft = Offset(inset, inset),
        size = Size(size.width - strokePx, size.height - strokePx),
        cornerRadius = CornerRadius(cornerRadius.toPx()),
        style = Stroke(width = strokePx, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))),
    )
}
