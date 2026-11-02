package ua.chnu.kmp.domain.cart.model

data class CartItem(
    val id: Long,
    val title: String,
    val priceUah: Int,
    val productId: Int? = null,
    val details: String? = null,
)
