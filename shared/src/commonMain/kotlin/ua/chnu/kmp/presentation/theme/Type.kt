package ua.chnu.kmp.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import ua.chnu.kmp.resources.Res
import ua.chnu.kmp.resources.ibm_plex_mono_bold
import ua.chnu.kmp.resources.ibm_plex_mono_medium
import ua.chnu.kmp.resources.rubik_bold
import ua.chnu.kmp.resources.rubik_medium
import ua.chnu.kmp.resources.rubik_mono_one
import ua.chnu.kmp.resources.rubik_regular

/** Rubik — body and titles. */
@Composable
fun rubikFamily() = FontFamily(
    Font(Res.font.rubik_regular, FontWeight.Normal),
    Font(Res.font.rubik_medium, FontWeight.Medium),
    Font(Res.font.rubik_bold, FontWeight.Bold),
)

/** Rubik Mono One — chunky display headlines. */
@Composable
fun displayFamily() = FontFamily(Font(Res.font.rubik_mono_one, FontWeight.Normal))

/** IBM Plex Mono — labels, prices, key legends. */
@Composable
fun monoFamily() = FontFamily(
    Font(Res.font.ibm_plex_mono_medium, FontWeight.Medium),
    Font(Res.font.ibm_plex_mono_bold, FontWeight.Bold),
)

@Composable
fun retroTypography(): Typography {
    val body = rubikFamily()
    val display = displayFamily()
    val mono = monoFamily()
    return Typography(
        displayLarge = TextStyle(fontFamily = display, fontSize = 34.sp, lineHeight = 40.sp),
        displayMedium = TextStyle(fontFamily = display, fontSize = 28.sp, lineHeight = 34.sp),
        displaySmall = TextStyle(fontFamily = display, fontSize = 23.sp, lineHeight = 28.sp),
        headlineLarge = TextStyle(fontFamily = display, fontSize = 22.sp, lineHeight = 28.sp),
        headlineMedium = TextStyle(fontFamily = display, fontSize = 18.sp, lineHeight = 24.sp),
        headlineSmall = TextStyle(fontFamily = display, fontSize = 15.sp, lineHeight = 20.sp),
        titleLarge = TextStyle(fontFamily = body, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 26.sp),
        titleMedium = TextStyle(fontFamily = body, fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 22.sp),
        titleSmall = TextStyle(fontFamily = body, fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 20.sp),
        bodyLarge = TextStyle(fontFamily = body, fontSize = 16.sp, lineHeight = 24.sp),
        bodyMedium = TextStyle(fontFamily = body, fontSize = 14.sp, lineHeight = 20.sp),
        bodySmall = TextStyle(fontFamily = body, fontSize = 12.sp, lineHeight = 16.sp),
        labelLarge = TextStyle(fontFamily = mono, fontWeight = FontWeight.Bold, fontSize = 13.sp, lineHeight = 16.sp, letterSpacing = 1.sp),
        labelMedium = TextStyle(fontFamily = mono, fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 1.2.sp),
        labelSmall = TextStyle(fontFamily = mono, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 14.sp),
    )
}
