package ua.chnu.kmp.domain.catalog.model

enum class ProductCategory { KEYBOARDS, MICE, HEADSETS, MATS }

data class Product(
    val id: Int,
    val name: String,
    val category: ProductCategory,
    val priceUah: Int,
    val tag: String,
)
