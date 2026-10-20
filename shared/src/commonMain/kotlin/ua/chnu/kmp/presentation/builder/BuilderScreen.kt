package ua.chnu.kmp.presentation.builder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.SolidColor
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
import ua.chnu.kmp.presentation.theme.RetroTheme
import ua.chnu.kmp.presentation.ui.ObserveEvents
import ua.chnu.kmp.presentation.ui.components.KeyboardPreview
import ua.chnu.kmp.presentation.ui.components.Keycap
import ua.chnu.kmp.presentation.ui.components.builderKeyboardRows
import ua.chnu.kmp.presentation.ui.components.dashedBorder
import ua.chnu.kmp.presentation.ui.formatUah
import ua.chnu.kmp.resources.Res
import ua.chnu.kmp.resources.builder_add
import ua.chnu.kmp.resources.builder_added
import ua.chnu.kmp.resources.builder_colorway
import ua.chnu.kmp.resources.builder_engraving
import ua.chnu.kmp.resources.builder_engraving_hint
import ua.chnu.kmp.resources.builder_profile
import ua.chnu.kmp.resources.builder_subtitle
import ua.chnu.kmp.resources.builder_switches
import ua.chnu.kmp.resources.builder_total
import ua.chnu.kmp.resources.catalog_added
import ua.chnu.kmp.resources.cd_back
import ua.chnu.kmp.resources.ic_back

/** Keyboard builder: colorway, switches, keycap profile, engraving — with a live preview. */
@Composable
fun BuilderScreen(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    viewModel: BuilderViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    // Resolve snackbar text in the app-selected language, not the system one.
    val environment = rememberResourceEnvironment()
    val colors = RetroTheme.colors

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            is BuilderEvent.AddedToCart -> scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(getString(environment, Res.string.catalog_added, event.title))
            }
        }
    }

    Column(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                val backLabel = stringResource(Res.string.cd_back)
                Keycap(
                    modifier = Modifier.size(width = 48.dp, height = 52.dp).semantics { contentDescription = backLabel },
                    onClick = onBack,
                    depth = 4.dp,
                ) {
                    Icon(painterResource(Res.drawable.ic_back), contentDescription = null, modifier = Modifier.size(22.dp))
                }
                Column {
                    Text("Forge 65", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
                    Text(stringResource(Res.string.builder_subtitle), style = MaterialTheme.typography.bodySmall, color = colors.muted)
                }
            }

            val stageShape = RoundedCornerShape(22.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(stageShape)
                    .background(colors.stage)
                    .border(2.dp, colors.outline, stageShape),
                contentAlignment = Alignment.Center,
            ) {
                KeyboardPreview(
                    rows = builderKeyboardRows(state.engravingLabel),
                    colors = state.colorway.keyboardColors(),
                    keyRadius = state.profile.keyRadius,
                    keyHeight = 28.dp,
                    modifier = Modifier.widthIn(max = 330.dp).fillMaxWidth(0.92f),
                )
            }

            Section(title = stringResource(Res.string.builder_colorway), trailing = state.colorway.name) {
                Row(Modifier.selectableGroup(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Colorways.forEachIndexed { index, colorway ->
                        Keycap(
                            modifier = Modifier.size(width = 48.dp, height = 52.dp).semantics { contentDescription = colorway.name },
                            onClick = { viewModel.onColorwaySelected(index) },
                            selected = index == state.colorwayIndex,
                            role = Role.RadioButton,
                            color = colorway.shell,
                            shape = RoundedCornerShape(12.dp),
                            depth = 4.dp,
                        ) {}
                    }
                }
            }

            Section(title = stringResource(Res.string.builder_switches)) {
                Row(Modifier.selectableGroup(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SwitchType.entries.forEach { type ->
                        val selected = type == state.switchType
                        Keycap(
                            modifier = Modifier.weight(1f).height(58.dp),
                            onClick = { viewModel.onSwitchSelected(type) },
                            selected = selected,
                            role = Role.RadioButton,
                            color = if (selected) colors.inverse else MaterialTheme.colorScheme.surface,
                            contentColor = if (selected) colors.onInverse else MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(12.dp),
                            depth = 4.dp,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(type.title, style = MaterialTheme.typography.titleSmall)
                                Text(stringResource(type.hint), style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp))
                            }
                        }
                    }
                }
            }

            Section(title = stringResource(Res.string.builder_profile)) {
                Row(Modifier.selectableGroup(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    KeycapProfile.entries.forEach { profile ->
                        val selected = profile == state.profile
                        Keycap(
                            modifier = Modifier.height(48.dp),
                            onClick = { viewModel.onProfileSelected(profile) },
                            selected = selected,
                            role = Role.RadioButton,
                            color = if (selected) colors.mustard else MaterialTheme.colorScheme.surface,
                            contentColor = if (selected) colors.onMustard else MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(10.dp),
                            depth = 4.dp,
                        ) {
                            Text(profile.title, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(horizontal = 18.dp))
                        }
                    }
                }
            }

            Section(title = stringResource(Res.string.builder_engraving)) {
                val engravingLabel = stringResource(Res.string.builder_engraving)
                BasicTextField(
                    value = state.engraving,
                    onValueChange = viewModel::onEngravingChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface),
                    cursorBrush = SolidColor(colors.rust),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .dashedBorder(colors.outline, 2.dp, 12.dp)
                        .semantics { contentDescription = engravingLabel },
                    decorationBox = { inner ->
                        Box(Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
                            if (state.engraving.isEmpty()) {
                                Text(stringResource(Res.string.builder_engraving_hint), style = MaterialTheme.typography.bodyLarge, color = colors.muted)
                            }
                            inner()
                        }
                    },
                )
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
                Text(stringResource(Res.string.builder_total), style = MaterialTheme.typography.bodySmall, color = colors.muted)
                Text(formatUah(state.totalUah), style = MaterialTheme.typography.labelLarge.copy(fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = 0.sp))
            }
            Keycap(
                modifier = Modifier.height(61.dp),
                onClick = viewModel::onAddToCart,
                color = if (state.justAdded) colors.teal else colors.rust,
                contentColor = if (state.justAdded) colors.onTeal else colors.onRust,
            ) {
                Text(
                    text = stringResource(if (state.justAdded) Res.string.builder_added else Res.string.builder_add),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 26.dp),
                )
            }
        }
    }
}

@Composable
private fun Section(title: String, trailing: String? = null, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth()) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
            if (trailing != null) {
                Text(trailing, style = MaterialTheme.typography.bodyMedium, color = RetroTheme.colors.muted)
            }
        }
        content()
    }
}
