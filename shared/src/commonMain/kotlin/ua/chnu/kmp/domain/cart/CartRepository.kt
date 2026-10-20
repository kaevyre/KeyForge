package ua.chnu.kmp.domain.cart

import kotlinx.coroutines.flow.StateFlow
import ua.chnu.kmp.domain.cart.model.CartItem

interface CartRepository {
    val items: StateFlow<List<CartItem>>

    fun add(title: String, priceUah: Int, productId: Int? = null, details: String? = null)
    fun remove(id: Long)
    fun clear()
}
