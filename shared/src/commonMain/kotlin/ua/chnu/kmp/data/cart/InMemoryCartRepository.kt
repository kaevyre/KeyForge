package ua.chnu.kmp.data.cart

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ua.chnu.kmp.domain.cart.CartRepository
import ua.chnu.kmp.domain.cart.model.CartItem

internal class InMemoryCartRepository : CartRepository {

    private val logger = Logger.withTag("Cart")
    private var nextId = 1L

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    override val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    override fun add(title: String, priceUah: Int, productId: Int?, details: String?) {
        val item = CartItem(nextId++, title, priceUah, productId, details)
        _items.update { it + item }
        logger.i { "Added $title ($priceUah UAH), items in cart: ${_items.value.size}" }
    }

    override fun remove(id: Long) {
        _items.update { items -> items.filterNot { it.id == id } }
        logger.d { "Removed item #$id" }
    }

    override fun clear() {
        logger.i { "Cart cleared (${_items.value.size} items)" }
        _items.value = emptyList()
    }
}
