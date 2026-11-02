package ua.chnu.kmp.presentation.onboarding

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.chnu.kmp.domain.language.change.ChangeLanguageUseCase
import ua.chnu.kmp.domain.language.obtain.ObtainLanguageUseCase
import ua.chnu.kmp.domain.onboarding.complete.CompleteOnboardingUseCase
import ua.chnu.kmp.domain.settings.model.AppLanguage

@Stable
class OnboardingViewModel internal constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val changeLanguageUseCase: ChangeLanguageUseCase,
    private val obtainLanguageUseCase: ObtainLanguageUseCase,
) : ViewModel() {

    private val logger = Logger.withTag("Onboarding")

    private val _state = MutableStateFlow(OnboardingState())
    internal val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _events = Channel<OnboardingEvent>(capacity = Channel.BUFFERED)
    val events: Flow<OnboardingEvent> = _events.receiveAsFlow()

    init {
        obtainLanguage()
    }

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.OnPageChanged -> changePage(action.page)
            is OnboardingAction.OnLanguageSelected -> changeLanguage(action.language)
            OnboardingAction.OnFinish -> finish()
        }
    }

    private fun changePage(page: Int) {
        if (_state.value.currentPage != page) {
            logger.d { "Onboarding page ${page + 1}" }
            _state.update { it.copy(currentPage = page) }
        }
    }

    private fun obtainLanguage() {
        viewModelScope.launch {
            obtainLanguageUseCase().collect { language ->
                _state.update { it.copy(language = language) }
            }
        }
    }

    private fun changeLanguage(language: AppLanguage) {
        logger.d { "Language selected: ${language.code}" }
        changeLanguageUseCase(language)
    }

    private fun finish() {
        logger.i { "Onboarding completed on page ${_state.value.currentPage + 1}" }
        completeOnboardingUseCase()
        _events.trySend(OnboardingEvent.NavigateToLogin)
    }
}
