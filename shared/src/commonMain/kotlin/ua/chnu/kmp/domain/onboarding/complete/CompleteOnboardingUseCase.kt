package ua.chnu.kmp.domain.onboarding.complete

import ua.chnu.kmp.domain.settings.PreferencesRepository

internal class CompleteOnboardingUseCase(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke() {
        preferencesRepository.setOnboardingCompleted(true)
    }
}
