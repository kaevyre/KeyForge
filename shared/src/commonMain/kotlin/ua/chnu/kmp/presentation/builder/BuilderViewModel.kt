package ua.chnu.kmp.presentation.builder

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import ua.chnu.kmp.domain.cart.CartRepository

@Immutable
data class BuilderState(
    val colorwayIndex: Int = 0,
    val switchType: SwitchType = SwitchType.TACTILE,
    val profile: KeycapProfile = KeycapProfile.SA,
    val engraving: String = "КЛАЦ",
    val justAdded: Boolean = false,
) {
    val colorway: Colorway get() = Colorways[colorwayIndex]

    val engravingLabel: String get() = engraving.trim().uppercase()

    val totalUah: Int
        get() = BASE_PRICE_UAH + switchType.surchargeUah + profile.surchargeUah +
            if (engravingLabel.isNotEmpty()) ENGRAVING_PRICE_UAH else 0

    val summary: String
        get() = listOfNotNull(
            colorway.name,
            switchType.title,
            profile.title,
            engravingLabel.takeIf { it.isNotEmpty() }?.let { "«$it»" },
        ).joinToString(" · ")
}

sealed interface BuilderEvent {
    data class AddedToCart(val title: String) : BuilderEvent
}

class BuilderViewModel internal constructor(
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val logger = Logger.withTag("Builder")

    private val _state = MutableStateFlow(BuilderState())
    val state: StateFlow<BuilderState> = _state.asStateFlow()

    private val _events = Channel<BuilderEvent>(capacity = Channel.BUFFERED)
    val events: Flow<BuilderEvent> = _events.receiveAsFlow()

    fun onColorwaySelected(index: Int) = edit { it.copy(colorwayIndex = index) }

    fun onSwitchSelected(type: SwitchType) = edit { it.copy(switchType = type) }

    fun onProfileSelected(profile: KeycapProfile) = edit { it.copy(profile = profile) }

    fun onEngravingChange(text: String) = edit { it.copy(engraving = text.take(MAX_ENGRAVING_LENGTH)) }

    fun onAddToCart() {
        val build = _state.value
        cartRepository.add(title = CUSTOM_BUILD_TITLE, priceUah = build.totalUah, details = build.summary)
        logger.i { "Custom build added: ${build.summary}, ${build.totalUah} UAH" }
        _state.update { it.copy(justAdded = true) }
        _events.trySend(BuilderEvent.AddedToCart(CUSTOM_BUILD_TITLE))
    }

    private fun edit(change: (BuilderState) -> BuilderState) {
        _state.update { change(it).copy(justAdded = false) }
    }

    private companion object {
        const val CUSTOM_BUILD_TITLE = "Forge 65 Custom"
        const val MAX_ENGRAVING_LENGTH = 14
    }
}
