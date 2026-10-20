package ua.chnu.kmp.presentation.catalog

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
import ua.chnu.kmp.domain.catalog.model.Product
import ua.chnu.kmp.domain.catalog.model.ProductCategory
import ua.chnu.kmp.domain.catalog.usecase.GetProductsUseCase

@Immutable
data class CatalogState(
    val query: String = "",
    val category: ProductCategory? = null,
    val products: List<Product> = emptyList(),
)

sealed interface CatalogEvent {
    data class AddedToCart(val productName: String) : CatalogEvent
}

class CatalogViewModel internal constructor(
    private val getProducts: GetProductsUseCase,
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val logger = Logger.withTag("Catalog")

    private val _state = MutableStateFlow(CatalogState(products = getProducts()))
    val state: StateFlow<CatalogState> = _state.asStateFlow()

    private val _events = Channel<CatalogEvent>(capacity = Channel.BUFFERED)
    val events: Flow<CatalogEvent> = _events.receiveAsFlow()

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query, products = getProducts(it.category, query)) }
    }

    fun onCategorySelected(category: ProductCategory?) {
        logger.d { "Filter by ${category ?: "ALL"}" }
        _state.update { it.copy(category = category, products = getProducts(category, it.query)) }
    }

    fun onAddToCart(product: Product) {
        cartRepository.add(title = product.name, priceUah = product.priceUah, productId = product.id)
        _events.trySend(CatalogEvent.AddedToCart(product.name))
    }
}
