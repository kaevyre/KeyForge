package ua.chnu.kmp.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ua.chnu.kmp.presentation.builder.BuilderScreen
import ua.chnu.kmp.presentation.cart.CartScreen
import ua.chnu.kmp.presentation.catalog.CatalogScreen
import ua.chnu.kmp.presentation.settings.SettingsScreen
import ua.chnu.kmp.presentation.theme.RetroTheme
import ua.chnu.kmp.presentation.ui.components.Keycap
import ua.chnu.kmp.resources.Res
import ua.chnu.kmp.resources.ic_cart
import ua.chnu.kmp.resources.ic_grid
import ua.chnu.kmp.resources.ic_settings
import ua.chnu.kmp.resources.ic_sliders
import ua.chnu.kmp.resources.nav_builder
import ua.chnu.kmp.resources.nav_cart
import ua.chnu.kmp.resources.nav_catalog
import ua.chnu.kmp.resources.nav_settings

enum class MainTab(val label: StringResource, val icon: DrawableResource) {
    CATALOG(Res.string.nav_catalog, Res.drawable.ic_grid),
    BUILDER(Res.string.nav_builder, Res.drawable.ic_sliders),
    CART(Res.string.nav_cart, Res.drawable.ic_cart),
    SETTINGS(Res.string.nav_settings, Res.drawable.ic_settings),
}

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val tab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val cartCount by viewModel.cartCount.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Keycap(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    color = RetroTheme.colors.inverse,
                    contentColor = RetroTheme.colors.onInverse,
                    depth = 4.dp,
                ) {
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                    )
                }
            }
        },
        bottomBar = {
            RetroBottomBar(selected = tab, cartCount = cartCount, onSelect = viewModel::selectTab)
        },
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding),
        ) {
            when (tab) {
                MainTab.CATALOG -> CatalogScreen(
                    snackbarHostState = snackbarHostState,
                    cartCount = cartCount,
                    onOpenBuilder = { viewModel.selectTab(MainTab.BUILDER) },
                    onOpenCart = { viewModel.selectTab(MainTab.CART) },
                )
                MainTab.BUILDER -> BuilderScreen(
                    snackbarHostState = snackbarHostState,
                    onBack = { viewModel.selectTab(MainTab.CATALOG) },
                )
                MainTab.CART -> CartScreen(
                    snackbarHostState = snackbarHostState,
                    onGoToCatalog = { viewModel.selectTab(MainTab.CATALOG) },
                )
                MainTab.SETTINGS -> SettingsScreen()
            }
        }
    }
}

@Composable
private fun RetroBottomBar(selected: MainTab, cartCount: Int, onSelect: (MainTab) -> Unit) {
    val colors = RetroTheme.colors
    Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
        Box(Modifier.fillMaxWidth().height(2.dp).background(colors.outline))
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(72.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            MainTab.entries.forEach { tab ->
                val isSelected = tab == selected
                val tint = if (isSelected) colors.rust else colors.muted
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .selectable(selected = isSelected, role = Role.Tab, onClick = { onSelect(tab) }),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box {
                        Icon(painterResource(tab.icon), contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
                        if (tab == MainTab.CART && cartCount > 0) {
                            CountBadge(cartCount, Modifier.align(Alignment.TopEnd).offset(x = 10.dp, y = (-8).dp))
                        }
                    }
                    Text(
                        text = stringResource(tab.label),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                        color = tint,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun CountBadge(count: Int, modifier: Modifier = Modifier) {
    val colors = RetroTheme.colors
    val shape = RoundedCornerShape(7.dp)
    Box(
        modifier = modifier
            .sizeIn(minWidth = 20.dp, minHeight = 20.dp)
            .clip(shape)
            .background(colors.rust)
            .border(2.dp, colors.outline, shape)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (count > 99) "99+" else count.toString(),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
            color = colors.onRust,
        )
    }
}
