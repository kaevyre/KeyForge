package ua.chnu.kmp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ua.chnu.kmp.domain.cart.CartRepository

class MainViewModel internal constructor(
    cartRepository: CartRepository,
) : ViewModel() {

    private val logger = Logger.withTag("Navigation")

    private val _selectedTab = MutableStateFlow(MainTab.CATALOG)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    val cartCount: StateFlow<Int> = cartRepository.items
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    fun selectTab(tab: MainTab) {
        if (_selectedTab.value != tab) {
            logger.d { "Open tab $tab" }
            _selectedTab.value = tab
        }
    }
}
