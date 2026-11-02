package ua.chnu.kmp.presentation.onboarding

import ua.chnu.kmp.domain.settings.model.AppLanguage

sealed class OnboardingAction {
    data class OnPageChanged(val page: Int) : OnboardingAction()
    data class OnLanguageSelected(val language: AppLanguage) : OnboardingAction()
    data object OnFinish : OnboardingAction()
}
