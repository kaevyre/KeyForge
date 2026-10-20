package ua.chnu.kmp.domain.catalog.usecase

import ua.chnu.kmp.domain.catalog.ProductRepository
import ua.chnu.kmp.domain.catalog.model.Product
import ua.chnu.kmp.domain.catalog.model.ProductCategory

class GetProductsUseCase(
    private val repository: ProductRepository,
) {
    operator fun invoke(category: ProductCategory? = null, query: String = ""): List<Product> {
        val needle = query.trim()
        return repository.getProducts().filter { product ->
            (category == null || product.category == category) &&
                (needle.isEmpty() || product.name.contains(needle, ignoreCase = true))
        }
    }
}
