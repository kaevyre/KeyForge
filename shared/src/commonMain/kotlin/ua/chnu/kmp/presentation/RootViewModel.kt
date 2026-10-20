package ua.chnu.kmp.presentation

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.chnu.kmp.domain.settings.PreferencesRepository
import ua.chnu.kmp.domain.settings.model.AppLanguage
import ua.chnu.kmp.domain.settings.model.UserPreferences

class RootViewModel internal constructor(
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val logger = Logger.withTag("Root")

    val preferences: StateFlow<UserPreferences> = preferencesRepository.preferences

    // Kept here (not in composition) so a language switch on the pager doesn't reset the page.
    private val _onboardingPage = MutableStateFlow(0)
    val onboardingPage: StateFlow<Int> = _onboardingPage.asStateFlow()

    fun onOnboardingPageChanged(page: Int) {
        if (_onboardingPage.value != page) {
            logger.d { "Onboarding page ${page + 1}" }
            _onboardingPage.value = page
        }
    }

    fun onLanguageSelected(language: AppLanguage) {
        preferencesRepository.setLanguage(language)
    }

    fun completeOnboarding() {
        logger.i { "Onboarding completed on page ${_onboardingPage.value + 1}" }
        _onboardingPage.value = 0
        preferencesRepository.setOnboardingCompleted(true)
    }
}
