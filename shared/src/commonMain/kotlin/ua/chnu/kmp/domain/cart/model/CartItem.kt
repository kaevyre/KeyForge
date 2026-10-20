package ua.chnu.kmp.domain.cart.model

data class CartItem(
    val id: Long,
    val title: String,
    val priceUah: Int,
    /** Catalog product this item comes from, or `null` for a custom build. */
    val productId: Int? = null,
    /** Configuration summary for custom builds. */
    val details: String? = null,
)
