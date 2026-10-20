package ua.chnu.kmp.presentation.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ua.chnu.kmp.domain.catalog.model.ProductCategory
import ua.chnu.kmp.domain.settings.model.AppLanguage
import ua.chnu.kmp.presentation.theme.RetroPalette
import ua.chnu.kmp.presentation.theme.RetroTheme
import ua.chnu.kmp.presentation.ui.components.KeyboardColors
import ua.chnu.kmp.presentation.ui.components.KeyboardPreview
import ua.chnu.kmp.presentation.ui.components.Keycap
import ua.chnu.kmp.presentation.ui.components.OnboardingKeyboardRows
import ua.chnu.kmp.presentation.ui.components.dashedBorder
import ua.chnu.kmp.presentation.ui.icon
import ua.chnu.kmp.presentation.ui.label
import ua.chnu.kmp.presentation.ui.shortLabel
import ua.chnu.kmp.presentation.ui.tileColors
import ua.chnu.kmp.resources.Res
import ua.chnu.kmp.resources.cd_back
import ua.chnu.kmp.resources.ic_arrow_right
import ua.chnu.kmp.resources.ic_back
import ua.chnu.kmp.resources.ic_check
import ua.chnu.kmp.resources.ic_package
import ua.chnu.kmp.resources.onb_engraving
import ua.chnu.kmp.resources.onb_language
import ua.chnu.kmp.resources.onb_next
import ua.chnu.kmp.resources.onb_skip
import ua.chnu.kmp.resources.onb_start
import ua.chnu.kmp.resources.onb_text_1
import ua.chnu.kmp.resources.onb_text_2
import ua.chnu.kmp.resources.onb_text_3
import ua.chnu.kmp.resources.onb_text_4
import ua.chnu.kmp.resources.onb_title_1
import ua.chnu.kmp.resources.onb_title_2
import ua.chnu.kmp.resources.onb_title_3
import ua.chnu.kmp.resources.onb_title_4
import ua.chnu.kmp.resources.order_step_assembly
import ua.chnu.kmp.resources.order_step_delivery
import ua.chnu.kmp.resources.order_step_testing

private data class OnboardingPage(val title: StringResource, val text: StringResource)

private val Pages = listOf(
    OnboardingPage(Res.string.onb_title_1, Res.string.onb_text_1),
    OnboardingPage(Res.string.onb_title_2, Res.string.onb_text_2),
    OnboardingPage(Res.string.onb_title_3, Res.string.onb_text_3),
    OnboardingPage(Res.string.onb_title_4, Res.string.onb_text_4),
)

/** Introduction to the app: 4 slides built on [HorizontalPager]. */
@Composable
fun OnboardingScreen(
    currentLanguage: AppLanguage,
    initialPage: Int,
    onPageChanged: (Int) -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
    onFinished: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = initialPage) { Pages.size }
    val scope = rememberCoroutineScope()
    val colors = RetroTheme.colors

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect(onPageChanged)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(52.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Logo()
            Spacer(Modifier.weight(1f))
            LanguageToggle(currentLanguage, onLanguageSelected)
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.onb_skip),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.muted,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(role = Role.Button, onClick = onFinished)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
            )
        }

        Spacer(Modifier.height(12.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            pageSpacing = 24.dp,
        ) { page ->
            Column(Modifier.fillMaxSize()) {
                val stageShape = RoundedCornerShape(28.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .heightIn(max = 420.dp)
                        .clip(stageShape)
                        .background(stageColor(page))
                        .border(2.dp, colors.outline, stageShape),
                    contentAlignment = Alignment.Center,
                ) {
                    when (page) {
                        0 -> KeyboardIllustration()
                        1 -> CategoriesIllustration()
                        2 -> CustomizeIllustration()
                        else -> DeliveryIllustration()
                    }
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "0${page + 1} / 0${Pages.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.rust,
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = stringResource(Pages[page].title),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    minLines = 2,
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = stringResource(Pages[page].text),
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.body,
                    minLines = 3,
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PagerIndicator(pageCount = Pages.size, currentPage = pagerState.currentPage)
            Spacer(Modifier.weight(1f))
            if (pagerState.currentPage > 0) {
                val backLabel = stringResource(Res.string.cd_back)
                Keycap(
                    modifier = Modifier.size(56.dp, 61.dp).semantics { contentDescription = backLabel },
                    onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                ) {
                    Icon(painterResource(Res.drawable.ic_back), contentDescription = null)
                }
                Spacer(Modifier.width(10.dp))
            }
            val isLast = pagerState.currentPage == Pages.lastIndex
            Keycap(
                modifier = Modifier.height(61.dp),
                onClick = {
                    if (isLast) onFinished()
                    else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                },
                color = if (isLast) colors.mustard else colors.rust,
                contentColor = if (isLast) colors.onMustard else colors.onRust,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource(if (isLast) Res.string.onb_start else Res.string.onb_next),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    if (!isLast) {
                        Icon(painterResource(Res.drawable.ic_arrow_right), contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun stageColor(page: Int): Color {
    val colors = RetroTheme.colors
    return when (page) {
        0 -> colors.mustard
        2 -> colors.teal
        else -> colors.stage
    }
}

@Composable
private fun Logo() {
    Text(
        text = buildAnnotatedString {
            append("KEY")
            withStyle(SpanStyle(color = RetroTheme.colors.rust)) { append("FORGE") }
        },
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun LanguageToggle(current: AppLanguage, onSelected: (AppLanguage) -> Unit) {
    val colors = RetroTheme.colors
    val groupLabel = stringResource(Res.string.onb_language)
    Row(
        modifier = Modifier.selectableGroup().semantics { contentDescription = groupLabel },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        AppLanguage.entries.forEach { language ->
            val selected = language == current
            Keycap(
                modifier = Modifier.size(width = 46.dp, height = 43.dp),
                onClick = { onSelected(language) },
                selected = selected,
                role = Role.RadioButton,
                color = if (selected) colors.inverse else MaterialTheme.colorScheme.surface,
                contentColor = if (selected) colors.onInverse else MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(10.dp),
                depth = 4.dp,
            ) {
                Text(language.shortLabel, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun PagerIndicator(pageCount: Int, currentPage: Int) {
    val colors = RetroTheme.colors
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(pageCount) { index ->
            val active = index == currentPage
            val width by animateDpAsState(if (active) 30.dp else 10.dp, label = "indicatorWidth")
            val shape = RoundedCornerShape(3.dp)
            Box(
                Modifier
                    .size(width = width, height = 10.dp)
                    .clip(shape)
                    .background(if (active) colors.rust else MaterialTheme.colorScheme.surface)
                    .border(2.dp, colors.outline, shape),
            )
        }
    }
}

// ---- Slide illustrations ----

@Composable
private fun KeyboardIllustration() {
    val colors = RetroTheme.colors
    KeyboardPreview(
        rows = OnboardingKeyboardRows,
        colors = KeyboardColors(
            shell = MaterialTheme.colorScheme.surface,
            alpha = colors.keycap,
            onAlpha = colors.onKeycap,
            mod = colors.modKey,
            onMod = colors.onModKey,
            accent = colors.rust,
            onAccent = colors.onRust,
            highlight = colors.mustard,
            onHighlight = colors.onMustard,
        ),
        keyHeight = 26.dp,
        modifier = Modifier.widthIn(max = 320.dp).fillMaxWidth(0.88f).rotate(-5f),
    )
}

@Composable
private fun CategoriesIllustration() {
    val categories = ProductCategory.entries
    Column(
        modifier = Modifier.widthIn(max = 320.dp).fillMaxWidth(0.85f),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        categories.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                row.forEach { category ->
                    val (bg, fg) = category.tileColors()
                    Keycap(
                        modifier = Modifier.weight(1f).height(126.dp),
                        color = bg,
                        contentColor = fg,
                        shape = RoundedCornerShape(18.dp),
                        depth = 6.dp,
                        contentAlignment = Alignment.TopStart,
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Icon(painterResource(category.icon), contentDescription = null, modifier = Modifier.size(34.dp))
                            Text(stringResource(category.label), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomizeIllustration() {
    val colors = RetroTheme.colors
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        Keycap(
            modifier = Modifier.size(width = 150.dp, height = 162.dp),
            color = colors.rust,
            contentColor = colors.onRust,
            shape = RoundedCornerShape(28.dp),
            depth = 12.dp,
            borderWidth = 3.dp,
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .size(width = 108.dp, height = 98.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(RetroPalette.RustLight),
                contentAlignment = Alignment.Center,
            ) {
                Text("Ї", style = MaterialTheme.typography.displayLarge.copy(fontSize = 46.sp))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf(colors.rust, colors.mustard, RetroPalette.Teal, RetroPalette.Beige, RetroPalette.Ink).forEach { swatch ->
                Keycap(
                    modifier = Modifier.size(width = 34.dp, height = 38.dp),
                    color = swatch,
                    shape = RoundedCornerShape(9.dp),
                    depth = 4.dp,
                ) {}
            }
        }
        Text(
            text = stringResource(Res.string.onb_engraving),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .dashedBorder(colors.outline, 2.dp, 10.dp)
                .padding(horizontal = 14.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun DeliveryIllustration() {
    val colors = RetroTheme.colors
    val steps = listOf(
        Res.string.order_step_assembly to true,
        Res.string.order_step_testing to true,
        Res.string.order_step_delivery to false,
    )
    Keycap(
        modifier = Modifier.widthIn(max = 320.dp).fillMaxWidth(0.85f),
        shape = RoundedCornerShape(22.dp),
        depth = 8.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                val boxShape = RoundedCornerShape(14.dp)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(boxShape)
                        .background(colors.mustard)
                        .border(2.dp, colors.outline, boxShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(painterResource(Res.drawable.ic_package), contentDescription = null, tint = colors.onMustard, modifier = Modifier.size(30.dp))
                }
                Column {
                    Text("#KF-0001", style = MaterialTheme.typography.labelSmall, color = colors.muted)
                    Text("Forge 65 · Rust", style = MaterialTheme.typography.titleMedium)
                }
            }
            steps.forEach { (title, done) ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    val shape = RoundedCornerShape(7.dp)
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(shape)
                            .background(if (done) colors.teal else MaterialTheme.colorScheme.surface)
                            .border(2.dp, colors.outline, shape),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (done) {
                            Icon(painterResource(Res.drawable.ic_check), contentDescription = null, tint = colors.onTeal, modifier = Modifier.size(14.dp))
                        }
                    }
                    Text(
                        text = stringResource(title),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (done) MaterialTheme.colorScheme.onSurface else colors.muted,
                    )
                }
            }
        }
    }
}
