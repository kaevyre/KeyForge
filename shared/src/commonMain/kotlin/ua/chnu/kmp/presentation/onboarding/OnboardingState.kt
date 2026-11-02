package ua.chnu.kmp.presentation.onboarding

import androidx.compose.runtime.Immutable
import ua.chnu.kmp.domain.settings.model.AppLanguage

@Immutable
internal data class OnboardingState(
    val currentPage: Int = 0,
    val language: AppLanguage? = null,
)
