package ua.chnu.kmp.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.rememberResourceEnvironment
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ua.chnu.kmp.domain.catalog.model.ProductCategory
import ua.chnu.kmp.presentation.theme.RetroTheme
import ua.chnu.kmp.presentation.ui.ObserveEvents
import ua.chnu.kmp.presentation.ui.components.Keycap
import ua.chnu.kmp.presentation.ui.formatUah
import ua.chnu.kmp.presentation.ui.icon
import ua.chnu.kmp.presentation.ui.productImage
import ua.chnu.kmp.presentation.ui.productSubtitle
import ua.chnu.kmp.presentation.ui.tileColors
import ua.chnu.kmp.resources.Res
import ua.chnu.kmp.resources.cart_checkout
import ua.chnu.kmp.resources.cart_empty_text
import ua.chnu.kmp.resources.cart_empty_title
import ua.chnu.kmp.resources.cart_go_catalog
import ua.chnu.kmp.resources.cart_items
import ua.chnu.kmp.resources.cart_order_placed
import ua.chnu.kmp.resources.cart_title
import ua.chnu.kmp.resources.cart_total
import ua.chnu.kmp.resources.cd_remove
import ua.chnu.kmp.resources.ic_cart
import ua.chnu.kmp.resources.ic_close

@Composable
fun CartScreen(
    snackbarHostState: SnackbarHostState,
    onGoToCatalog: () -> Unit,
    viewModel: CartViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    // Resolve snackbar text in the app-selected language, not the system one.
    val environment = rememberResourceEnvironment()
    val colors = RetroTheme.colors

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            CartEvent.OrderPlaced -> scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(getString(environment, Res.string.cart_order_placed))
            }
        }
    }

    if (state.lines.isEmpty()) {
        EmptyCart(onGoToCatalog)
        return
    }

    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 16.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = stringResource(Res.string.cart_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
            )
            Text(stringResource(Res.string.cart_items, state.lines.size), style = MaterialTheme.typography.labelMedium, color = colors.muted)
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(state.lines, key = { it.item.id }) { line ->
                CartRow(line, onRemove = { viewModel.onRemove(line.item.id) })
            }
        }
        Box(Modifier.fillMaxWidth().height(2.dp).background(colors.outline))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(Res.string.cart_total), style = MaterialTheme.typography.bodySmall, color = colors.muted)
                Text(formatUah(state.totalUah), style = MaterialTheme.typography.labelLarge.copy(fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = 0.sp))
            }
            Keycap(
                modifier = Modifier.height(61.dp),
                onClick = viewModel::onCheckout,
                color = colors.rust,
                contentColor = colors.onRust,
            ) {
                Text(
                    text = stringResource(Res.string.cart_checkout),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
    }
}

@Composable
private fun CartRow(line: CartLine, onRemove: () -> Unit) {
    val colors = RetroTheme.colors
    val (tileBg, tileFg) = line.category.tileColors()
    val removeLabel = stringResource(Res.string.cd_remove, line.item.title)
    Keycap(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        depth = 4.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val tileShape = RoundedCornerShape(12.dp)
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(tileShape)
                    .background(tileBg)
                    .border(2.dp, colors.outline, tileShape),
                contentAlignment = Alignment.Center,
            ) {
                val photo = line.item.productId?.let(::productImage)
                if (photo != null) {
                    Image(
                        painter = painterResource(photo),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().padding(2.dp).clip(RoundedCornerShape(10.dp)),
                    )
                } else {
                    Icon(
                        painter = painterResource((line.category ?: ProductCategory.KEYBOARDS).icon),
                        contentDescription = null,
                        tint = tileFg,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }
            Column(Modifier.weight(1f)) {
                Text(line.item.title, style = MaterialTheme.typography.titleSmall)
                val details = line.item.details
                    ?: line.item.productId?.let(::productSubtitle)?.let { stringResource(it) }
                if (details != null) {
                    Text(details, style = MaterialTheme.typography.bodySmall, color = colors.muted)
                }
                Text(
                    text = formatUah(line.item.priceUah),
                    style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 0.sp),
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Keycap(
                modifier = Modifier.size(width = 44.dp, height = 47.dp).semantics { contentDescription = removeLabel },
                onClick = onRemove,
                shape = RoundedCornerShape(10.dp),
                depth = 3.dp,
            ) {
                Icon(painterResource(Res.drawable.ic_close), contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun EmptyCart(onGoToCatalog: () -> Unit) {
    val colors = RetroTheme.colors
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Keycap(
            modifier = Modifier.size(width = 96.dp, height = 104.dp),
            color = colors.mustard,
            contentColor = colors.onMustard,
            shape = RoundedCornerShape(22.dp),
            depth = 8.dp,
        ) {
            Icon(painterResource(Res.drawable.ic_cart), contentDescription = null, modifier = Modifier.size(42.dp))
        }
        Text(
            text = stringResource(Res.string.cart_empty_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 24.dp),
        )
        Text(
            text = stringResource(Res.string.cart_empty_text),
            style = MaterialTheme.typography.bodyLarge,
            color = colors.body,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, bottom = 24.dp),
        )
        Keycap(
            modifier = Modifier.height(57.dp),
            onClick = onGoToCatalog,
            color = colors.rust,
            contentColor = colors.onRust,
        ) {
            Text(
                text = stringResource(Res.string.cart_go_catalog),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
        }
    }
}
