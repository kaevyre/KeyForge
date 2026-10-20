package ua.chnu.kmp.data.settings

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.chnu.kmp.domain.settings.PreferencesRepository
import ua.chnu.kmp.domain.settings.model.AppLanguage
import ua.chnu.kmp.domain.settings.model.ThemeMode
import ua.chnu.kmp.domain.settings.model.UserPreferences

/**
 * Stores user preferences with multiplatform-settings
 * (SharedPreferences / NSUserDefaults / java.util.prefs / localStorage).
 */
internal class AppPreferencesRepository(
    private val settings: Settings,
) : PreferencesRepository {

    private val logger = Logger.withTag("Preferences")

    private val _preferences = MutableStateFlow(read())
    override val preferences: StateFlow<UserPreferences> = _preferences.asStateFlow()

    init {
        logger.i { "Loaded preferences: ${_preferences.value}" }
    }

    override fun setLanguage(language: AppLanguage) {
        settings.putString(KEY_LANGUAGE, language.code)
        refresh("language = ${language.code}")
    }

    override fun setThemeMode(mode: ThemeMode) {
        settings.putString(KEY_THEME, mode.name)
        refresh("theme = $mode")
    }

    override fun setNotificationsEnabled(enabled: Boolean) {
        settings.putBoolean(KEY_NOTIFICATIONS, enabled)
        refresh("notifications = $enabled")
    }

    override fun setOnboardingCompleted(completed: Boolean) {
        settings.putBoolean(KEY_ONBOARDING, completed)
        refresh("onboardingCompleted = $completed")
    }

    private fun refresh(change: String) {
        _preferences.value = read()
        logger.d { "Saved $change" }
    }

    private fun read() = UserPreferences(
        language = settings.getStringOrNull(KEY_LANGUAGE)?.let(AppLanguage::fromCode),
        themeMode = settings.getStringOrNull(KEY_THEME)
            ?.let { name -> ThemeMode.entries.firstOrNull { it.name == name } }
            ?: ThemeMode.SYSTEM,
        notificationsEnabled = settings.getBoolean(KEY_NOTIFICATIONS, true),
        onboardingCompleted = settings.getBoolean(KEY_ONBOARDING, false),
    )

    private companion object {
        const val KEY_LANGUAGE = "app_language"
        const val KEY_THEME = "theme_mode"
        const val KEY_NOTIFICATIONS = "notifications_enabled"
        const val KEY_ONBOARDING = "onboarding_completed"
    }
}
