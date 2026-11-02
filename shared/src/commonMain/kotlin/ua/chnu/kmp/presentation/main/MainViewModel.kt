package ua.chnu.kmp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ua.chnu.kmp.domain.cart.CartRepository
import ua.chnu.kmp.domain.onboarding.check.CheckOnboardingUseCase

class MainViewModel internal constructor(
    cartRepository: CartRepository,
    private val checkOnboardingUseCase: CheckOnboardingUseCase,
) : ViewModel() {

    private val logger = Logger.withTag("Navigation")

    private val _selectedTab = MutableStateFlow(MainTab.CATALOG)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    val cartCount: StateFlow<Int> = cartRepository.items
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    private val _events = Channel<MainEvent>(capacity = Channel.BUFFERED)
    val events: Flow<MainEvent> = _events.receiveAsFlow()

    init {
        checkOnboarding()
    }

    fun selectTab(tab: MainTab) {
        if (_selectedTab.value != tab) {
            logger.d { "Open tab $tab" }
            _selectedTab.value = tab
        }
    }

    private fun checkOnboarding() {
        viewModelScope.launch {
            checkOnboardingUseCase().collect { completed ->
                if (!completed) {
                    logger.i { "Onboarding is not completed, open it" }
                    _events.trySend(MainEvent.NavigateToOnboarding)
                }
            }
        }
    }
}
