package ua.chnu.kmp.presentation.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.key

/**
 * Overrides the locale used by Compose resources, so the language can be switched inside the app.
 * See https://kotlinlang.org/docs/multiplatform/compose-resource-environment.html
 */
expect object LocalAppLocale {
    val current: String @Composable get

    @Composable
    infix fun provides(value: String?): ProvidedValue<*>
}

/** Provides [languageCode] (or the system language when `null`) to everything inside. */
@Composable
fun AppEnvironment(languageCode: String?, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAppLocale provides languageCode) {
        // Re-creates the tree so string resources are re-read for the new locale.
        key(languageCode) {
            content()
        }
    }
}
