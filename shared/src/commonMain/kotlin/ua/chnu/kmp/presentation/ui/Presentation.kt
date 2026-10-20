package ua.chnu.kmp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import ua.chnu.kmp.domain.catalog.model.ProductCategory
import ua.chnu.kmp.domain.settings.model.AppLanguage
import ua.chnu.kmp.presentation.localization.LocalAppLocale
import ua.chnu.kmp.presentation.theme.RetroTheme
import ua.chnu.kmp.resources.Res
import ua.chnu.kmp.resources.cat_headsets
import ua.chnu.kmp.resources.cat_keyboards
import ua.chnu.kmp.resources.cat_mats
import ua.chnu.kmp.resources.cat_mice
import ua.chnu.kmp.resources.ic_headphones
import ua.chnu.kmp.resources.ic_keyboard
import ua.chnu.kmp.resources.ic_mat
import ua.chnu.kmp.resources.ic_mouse
import ua.chnu.kmp.resources.product_echo_headset
import ua.chnu.kmp.resources.product_echo_studio
import ua.chnu.kmp.resources.product_forge_65
import ua.chnu.kmp.resources.product_forge_tkl
import ua.chnu.kmp.resources.product_grid_retro
import ua.chnu.kmp.resources.product_grid_xl
import ua.chnu.kmp.resources.product_pulse_mini
import ua.chnu.kmp.resources.product_pulse_mouse
import ua.chnu.kmp.resources.sub_echo
import ua.chnu.kmp.resources.sub_echo_studio
import ua.chnu.kmp.resources.sub_forge_65
import ua.chnu.kmp.resources.sub_forge_tkl
import ua.chnu.kmp.resources.sub_grid_retro
import ua.chnu.kmp.resources.sub_grid_xl
import ua.chnu.kmp.resources.sub_pulse
import ua.chnu.kmp.resources.sub_pulse_mini

/** "12 345 ₴" with non-breaking spaces. */
fun formatUah(amount: Int): String {
    val grouped = amount.toString().reversed().chunked(3).joinToString(" ").reversed()
    return "$grouped ₴"
}

val ProductCategory.label: StringResource
    get() = when (this) {
        ProductCategory.KEYBOARDS -> Res.string.cat_keyboards
        ProductCategory.MICE -> Res.string.cat_mice
        ProductCategory.HEADSETS -> Res.string.cat_headsets
        ProductCategory.MATS -> Res.string.cat_mats
    }

val ProductCategory.icon: DrawableResource
    get() = when (this) {
        ProductCategory.KEYBOARDS -> Res.drawable.ic_keyboard
        ProductCategory.MICE -> Res.drawable.ic_mouse
        ProductCategory.HEADSETS -> Res.drawable.ic_headphones
        ProductCategory.MATS -> Res.drawable.ic_mat
    }

/** Background and content color of a product tile. */
@Composable
fun ProductCategory?.tileColors(): Pair<Color, Color> {
    val colors = RetroTheme.colors
    return when (this) {
        ProductCategory.KEYBOARDS, null -> colors.rust to colors.onRust
        ProductCategory.MICE -> colors.keycap to colors.onKeycap
        ProductCategory.HEADSETS -> colors.teal to colors.onTeal
        ProductCategory.MATS -> colors.mustard to colors.onMustard
    }
}

/** Product photo (Unsplash, see README), or `null` for custom builds. */
fun productImage(productId: Int): DrawableResource? = when (productId) {
    1 -> Res.drawable.product_forge_65
    2 -> Res.drawable.product_forge_tkl
    3 -> Res.drawable.product_pulse_mouse
    4 -> Res.drawable.product_pulse_mini
    5 -> Res.drawable.product_echo_headset
    6 -> Res.drawable.product_echo_studio
    7 -> Res.drawable.product_grid_xl
    8 -> Res.drawable.product_grid_retro
    else -> null
}

/** Localized one-line spec for a catalog product. */
fun productSubtitle(productId: Int): StringResource? = when (productId) {
    1 -> Res.string.sub_forge_65
    2 -> Res.string.sub_forge_tkl
    3 -> Res.string.sub_pulse
    4 -> Res.string.sub_pulse_mini
    5 -> Res.string.sub_echo
    6 -> Res.string.sub_echo_studio
    7 -> Res.string.sub_grid_xl
    8 -> Res.string.sub_grid_retro
    else -> null
}

/** The language actually shown: the saved one, or the system one when nothing is saved. */
@Composable
fun effectiveLanguage(selected: AppLanguage?): AppLanguage =
    selected ?: if (LocalAppLocale.current.lowercase().startsWith("uk")) AppLanguage.UKRAINIAN else AppLanguage.ENGLISH

val AppLanguage.shortLabel: String
    get() = when (this) {
        AppLanguage.UKRAINIAN -> "UA"
        AppLanguage.ENGLISH -> "EN"
    }
