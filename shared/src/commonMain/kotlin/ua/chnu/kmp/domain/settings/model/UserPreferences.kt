package ua.chnu.kmp.domain.settings.model

enum class AppLanguage(val code: String) {
    UKRAINIAN("uk"),
    ENGLISH("en");

    companion object {
        fun fromCode(code: String): AppLanguage? = entries.firstOrNull { it.code == code }
    }
}

enum class ThemeMode { SYSTEM, LIGHT, DARK }

data class UserPreferences(
    /** `null` means "follow the system language". */
    val language: AppLanguage? = null,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val notificationsEnabled: Boolean = true,
    val onboardingCompleted: Boolean = false,
)
