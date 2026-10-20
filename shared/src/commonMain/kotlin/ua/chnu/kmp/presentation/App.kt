package ua.chnu.kmp.presentation

import androidx.compose.animation.Crossfade
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
import ua.chnu.kmp.presentation.main.MainScreen
import ua.chnu.kmp.presentation.onboarding.OnboardingScreen
import ua.chnu.kmp.presentation.theme.KeyForgeTheme
import ua.chnu.kmp.presentation.ui.effectiveLanguage

@Composable
fun App(
    viewModel: RootViewModel = koinViewModel(),
) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val onboardingPage by viewModel.onboardingPage.collectAsStateWithLifecycle()

    AppEnvironment(languageCode = preferences.language?.code) {
        val darkTheme = when (preferences.themeMode) {
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
        }
        KeyForgeTheme(darkTheme = darkTheme) {
            Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                Crossfade(targetState = preferences.onboardingCompleted, label = "rootScreen") { completed ->
                    if (completed) {
                        MainScreen()
                    } else {
                        OnboardingScreen(
                            currentLanguage = effectiveLanguage(preferences.language),
                            initialPage = onboardingPage,
                            onPageChanged = viewModel::onOnboardingPageChanged,
                            onLanguageSelected = viewModel::onLanguageSelected,
                            onFinished = viewModel::completeOnboarding,
                        )
                    }
                }
            }
        }
    }
}
