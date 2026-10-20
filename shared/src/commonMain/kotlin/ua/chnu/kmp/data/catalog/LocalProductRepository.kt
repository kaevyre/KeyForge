package ua.chnu.kmp.data.catalog

import ua.chnu.kmp.domain.catalog.ProductRepository
import ua.chnu.kmp.domain.catalog.model.Product
import ua.chnu.kmp.domain.catalog.model.ProductCategory

/** Static demo catalog. Prices are sample values. */
internal class LocalProductRepository : ProductRepository {

    override fun getProducts(): List<Product> = PRODUCTS

    private companion object {
        val PRODUCTS = listOf(
            Product(1, "Forge 65", ProductCategory.KEYBOARDS, 4899, "CUSTOM"),
            Product(2, "Forge 96", ProductCategory.KEYBOARDS, 5699, "NEW"),
            Product(3, "Pulse Mouse", ProductCategory.MICE, 2199, "HIT"),
            Product(4, "Pulse Mini", ProductCategory.MICE, 1799, "LIGHT"),
            Product(5, "Echo Headset", ProductCategory.HEADSETS, 3499, "HIT"),
            Product(6, "Echo Studio", ProductCategory.HEADSETS, 4299, "PRO"),
            Product(7, "Grid XL", ProductCategory.MATS, 899, "PRINT"),
            Product(8, "Grid Retro", ProductCategory.MATS, 649, "RETRO"),
        )
    }
}
