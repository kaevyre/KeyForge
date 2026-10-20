package ua.chnu.kmp.presentation.cart

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import ua.chnu.kmp.domain.cart.CartRepository
import ua.chnu.kmp.domain.cart.model.CartItem
import ua.chnu.kmp.domain.catalog.ProductRepository
import ua.chnu.kmp.domain.catalog.model.ProductCategory

@Immutable
data class CartLine(
    val item: CartItem,
    /** `null` for custom builds. */
    val category: ProductCategory?,
)

@Immutable
data class CartState(
    val lines: List<CartLine> = emptyList(),
) {
    val totalUah: Int get() = lines.sumOf { it.item.priceUah }
}

sealed interface CartEvent {
    data object OrderPlaced : CartEvent
}

class CartViewModel internal constructor(
    private val cartRepository: CartRepository,
    productRepository: ProductRepository,
) : ViewModel() {

    private val logger = Logger.withTag("Cart")
    private val categories = productRepository.getProducts().associate { it.id to it.category }

    val state: StateFlow<CartState> = cartRepository.items
        .map { items -> CartState(items.map { CartLine(it, it.productId?.let(categories::get)) }) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, CartState())

    private val _events = Channel<CartEvent>(capacity = Channel.BUFFERED)
    val events: Flow<CartEvent> = _events.receiveAsFlow()

    fun onRemove(id: Long) = cartRepository.remove(id)

    fun onCheckout() {
        val current = state.value
        if (current.lines.isEmpty()) return
        logger.i { "Order placed: ${current.lines.size} items, ${current.totalUah} UAH" }
        cartRepository.clear()
        _events.trySend(CartEvent.OrderPlaced)
    }
}
