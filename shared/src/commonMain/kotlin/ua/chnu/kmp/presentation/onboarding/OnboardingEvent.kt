package ua.chnu.kmp.presentation.onboarding

sealed class OnboardingEvent {
    data object NavigateToLogin : OnboardingEvent()
}
