package ua.chnu.kmp.presentation.settings

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.StateFlow
import ua.chnu.kmp.domain.settings.PreferencesRepository
import ua.chnu.kmp.domain.settings.model.AppLanguage
import ua.chnu.kmp.domain.settings.model.ThemeMode
import ua.chnu.kmp.domain.settings.model.UserPreferences

class SettingsViewModel internal constructor(
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val logger = Logger.withTag("Settings")

    val preferences: StateFlow<UserPreferences> = preferencesRepository.preferences

    fun onLanguageSelected(language: AppLanguage) = preferencesRepository.setLanguage(language)

    fun onThemeSelected(mode: ThemeMode) = preferencesRepository.setThemeMode(mode)

    fun onNotificationsChanged(enabled: Boolean) = preferencesRepository.setNotificationsEnabled(enabled)

    fun onShowOnboardingAgain() {
        logger.i { "User asked to see onboarding again" }
        preferencesRepository.setOnboardingCompleted(false)
    }
}
