package ua.chnu.kmp.domain.settings

import kotlinx.coroutines.flow.StateFlow
import ua.chnu.kmp.domain.settings.model.AppLanguage
import ua.chnu.kmp.domain.settings.model.ThemeMode
import ua.chnu.kmp.domain.settings.model.UserPreferences

interface PreferencesRepository {
    val preferences: StateFlow<UserPreferences>

    fun setLanguage(language: AppLanguage)
    fun setThemeMode(mode: ThemeMode)
    fun setNotificationsEnabled(enabled: Boolean)
    fun setOnboardingCompleted(completed: Boolean)
}
