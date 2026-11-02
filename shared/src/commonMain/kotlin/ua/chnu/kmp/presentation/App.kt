package ua.chnu.kmp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ua.chnu.kmp.domain.settings.model.ThemeMode
import ua.chnu.kmp.presentation.localization.AppEnvironment
import ua.chnu.kmp.presentation.navigation.AppNavigation
import ua.chnu.kmp.presentation.theme.KeyForgeTheme

@Composable
fun App(
    viewModel: RootViewModel = koinViewModel(),
) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()

    AppEnvironment(languageCode = preferences.language?.code) {
        val darkTheme = when (preferences.themeMode) {
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
        }
        KeyForgeTheme(darkTheme = darkTheme) {
            Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                AppNavigation()
            }
        }
    }
}
