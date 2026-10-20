package ua.chnu.kmp.presentation.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.rememberResourceEnvironment
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ua.chnu.kmp.domain.catalog.model.Product
import ua.chnu.kmp.domain.catalog.model.ProductCategory
import ua.chnu.kmp.presentation.main.CountBadge
import ua.chnu.kmp.presentation.theme.RetroPalette
import ua.chnu.kmp.presentation.theme.RetroTheme
import ua.chnu.kmp.presentation.ui.ObserveEvents
import ua.chnu.kmp.presentation.ui.components.Keycap
import ua.chnu.kmp.presentation.ui.formatUah
import ua.chnu.kmp.presentation.ui.icon
import ua.chnu.kmp.presentation.ui.label
import ua.chnu.kmp.presentation.ui.productImage
import ua.chnu.kmp.presentation.ui.productSubtitle
import ua.chnu.kmp.presentation.ui.tileColors
import ua.chnu.kmp.resources.Res
import ua.chnu.kmp.resources.cat_all
import ua.chnu.kmp.resources.catalog_added
import ua.chnu.kmp.resources.catalog_banner_label
import ua.chnu.kmp.resources.catalog_banner_text
import ua.chnu.kmp.resources.catalog_banner_title
import ua.chnu.kmp.resources.catalog_clear
import ua.chnu.kmp.resources.catalog_empty
import ua.chnu.kmp.resources.catalog_greeting
import ua.chnu.kmp.resources.catalog_popular
import ua.chnu.kmp.resources.catalog_search
import ua.chnu.kmp.resources.catalog_search_hint
import ua.chnu.kmp.resources.catalog_title
import ua.chnu.kmp.resources.cd_add_to_cart
import ua.chnu.kmp.resources.cd_open_cart
import ua.chnu.kmp.resources.ic_cart
import ua.chnu.kmp.resources.ic_close
import ua.chnu.kmp.resources.ic_plus
import ua.chnu.kmp.resources.ic_search

@Composable
fun CatalogScreen(
    snackbarHostState: SnackbarHostState,
    cartCount: Int,
    onOpenBuilder: () -> Unit,
    onOpenCart: () -> Unit,
    viewModel: CatalogViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    // Resolve snackbar text in the app-selected language, not the system one.
    val environment = rememberResourceEnvironment()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            is CatalogEvent.AddedToCart -> scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(getString(environment, Res.string.catalog_added, event.productName))
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) { Header(cartCount, onOpenCart) }
        item(span = { GridItemSpan(maxLineSpan) }) { SearchField(state.query, viewModel::onQueryChange) }
        item(span = { GridItemSpan(maxLineSpan) }) { CategoryChips(state.category, viewModel::onCategorySelected) }
        if (state.category == null && state.query.isBlank()) {
            item(span = { GridItemSpan(maxLineSpan) }) { BuilderBanner(onOpenBuilder) }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = stringResource(state.category?.label ?: Res.string.catalog_popular),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        if (state.products.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = stringResource(Res.string.catalog_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    color = RetroTheme.colors.muted,
                    modifier = Modifier.padding(vertical = 24.dp),
                )
            }
        }
        items(state.products, key = { it.id }) { product ->
            ProductCard(product = product, onAdd = { viewModel.onAddToCart(product) })
        }
    }
}

@Composable
private fun Header(cartCount: Int, onOpenCart: () -> Unit) {
    val cartLabel = stringResource(Res.string.cd_open_cart, cartCount)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(stringResource(Res.string.catalog_greeting), style = MaterialTheme.typography.bodySmall, color = RetroTheme.colors.muted)
            Text(stringResource(Res.string.catalog_title), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        }
        Box {
            Keycap(
                modifier = Modifier.size(width = 48.dp, height = 52.dp).semantics { contentDescription = cartLabel },
                onClick = onOpenCart,
                depth = 4.dp,
            ) {
                Icon(painterResource(Res.drawable.ic_cart), contentDescription = null, modifier = Modifier.size(22.dp))
            }
            if (cartCount > 0) {
                CountBadge(cartCount, Modifier.align(Alignment.TopEnd).offset(x = 6.dp, y = (-6).dp))
            }
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    val colors = RetroTheme.colors
    val shape = RoundedCornerShape(14.dp)
    val searchLabel = stringResource(Res.string.catalog_search)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, colors.outline, shape)
            .padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painterResource(Res.drawable.ic_search), contentDescription = null, tint = colors.muted, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(colors.rust),
            modifier = Modifier.weight(1f).semantics { contentDescription = searchLabel },
            decorationBox = { inner ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (query.isEmpty()) {
                        Text(stringResource(Res.string.catalog_search_hint), style = MaterialTheme.typography.bodyLarge, color = colors.muted)
                    }
                    inner()
                }
            },
        )
        if (query.isNotEmpty()) {
            val clearLabel = stringResource(Res.string.catalog_clear)
            Keycap(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(width = 36.dp, height = 38.dp)
                    .semantics { contentDescription = clearLabel },
                onClick = { onQueryChange("") },
                shape = RoundedCornerShape(8.dp),
                depth = 2.dp,
            ) {
                Icon(painterResource(Res.drawable.ic_close), contentDescription = null, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
private fun CategoryChips(selected: ProductCategory?, onSelect: (ProductCategory?) -> Unit) {
    val colors = RetroTheme.colors
    val options: List<ProductCategory?> = listOf(null) + ProductCategory.entries
    LazyRow(
        modifier = Modifier.selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(options) { category ->
            val isSelected = category == selected
            Keycap(
                modifier = Modifier.height(43.dp),
                onClick = { onSelect(category) },
                selected = isSelected,
                role = Role.RadioButton,
                color = if (isSelected) colors.inverse else MaterialTheme.colorScheme.surface,
                contentColor = if (isSelected) colors.onInverse else MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(10.dp),
                depth = 3.dp,
            ) {
                Text(
                    text = stringResource(category?.label ?: Res.string.cat_all),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun BuilderBanner(onClick: () -> Unit) {
    val colors = RetroTheme.colors
    Keycap(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = colors.mustard,
        contentColor = colors.onMustard,
        shape = RoundedCornerShape(20.dp),
        depth = 6.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(stringResource(Res.string.catalog_banner_label), style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp))
                Text(stringResource(Res.string.catalog_banner_title), style = MaterialTheme.typography.headlineSmall)
                Text(stringResource(Res.string.catalog_banner_text), style = MaterialTheme.typography.bodySmall)
            }
            Column(
                modifier = Modifier.width(76.dp).rotate(8f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                listOf(
                    listOf(RetroPalette.Rust, RetroPalette.Paper),
                    listOf(RetroPalette.Teal, RetroPalette.Stone),
                ).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        row.forEach { swatch ->
                            Keycap(Modifier.weight(1f).height(37.dp), color = swatch, shape = RoundedCornerShape(8.dp), depth = 3.dp) {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(product: Product, onAdd: () -> Unit) {
    val colors = RetroTheme.colors
    val (tileBg, tileFg) = product.category.tileColors()
    val addLabel = stringResource(Res.string.cd_add_to_cart, product.name)
    Keycap(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        depth = 5.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val tileShape = RoundedCornerShape(12.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(116.dp)
                    .clip(tileShape)
                    .background(tileBg)
                    .border(2.dp, colors.outline, tileShape),
                contentAlignment = Alignment.Center,
            ) {
                val photo = productImage(product.id)
                if (photo != null) {
                    Image(
                        painter = painterResource(photo),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().padding(2.dp).clip(RoundedCornerShape(10.dp)),
                    )
                } else {
                    Icon(painterResource(product.category.icon), contentDescription = null, tint = tileFg, modifier = Modifier.size(54.dp))
                }
                Text(
                    text = product.tag,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = colors.onInverse,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(colors.inverse)
                        .padding(horizontal = 7.dp, vertical = 3.dp),
                )
            }
            Column(Modifier.padding(horizontal = 2.dp)) {
                Text(product.name, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                productSubtitle(product.id)?.let {
                    Text(stringResource(it), style = MaterialTheme.typography.bodySmall, color = colors.muted, maxLines = 1)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatUah(product.priceUah),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp, letterSpacing = 0.sp),
                    modifier = Modifier.weight(1f),
                )
                Keycap(
                    modifier = Modifier.size(width = 40.dp, height = 43.dp).semantics { contentDescription = addLabel },
                    onClick = onAdd,
                    color = colors.rust,
                    contentColor = colors.onRust,
                    shape = RoundedCornerShape(10.dp),
                    depth = 3.dp,
                ) {
                    Icon(painterResource(Res.drawable.ic_plus), contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
