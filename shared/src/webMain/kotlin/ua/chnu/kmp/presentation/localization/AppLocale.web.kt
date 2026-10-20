package ua.chnu.kmp.presentation.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.intl.Locale

/** `window.__customLocale` is read by the `navigator.languages` override in index.html. */
external object window {
    @Suppress("PropertyName")
    var __customLocale: String?
}

actual object LocalAppLocale {
    private val LocalAppLocale = staticCompositionLocalOf { Locale.current }

    actual val current: String
        @Composable get() = LocalAppLocale.current.toString()

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        window.__customLocale = value?.replace('_', '-')
        return LocalAppLocale.provides(Locale.current)
    }
}
