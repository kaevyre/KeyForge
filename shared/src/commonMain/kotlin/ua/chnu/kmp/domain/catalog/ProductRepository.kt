package ua.chnu.kmp.domain.catalog

import ua.chnu.kmp.domain.catalog.model.Product

interface ProductRepository {
    fun getProducts(): List<Product>
}
