package ua.chnu.kmp.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ua.chnu.kmp.domain.settings.model.AppLanguage
import ua.chnu.kmp.domain.settings.model.ThemeMode
import ua.chnu.kmp.presentation.theme.RetroTheme
import ua.chnu.kmp.presentation.ui.components.Keycap
import ua.chnu.kmp.presentation.ui.components.RetroSwitch
import ua.chnu.kmp.presentation.ui.effectiveLanguage
import ua.chnu.kmp.presentation.ui.shortLabel
import ua.chnu.kmp.resources.Res
import ua.chnu.kmp.resources.ic_chevron_right
import ua.chnu.kmp.resources.lang_en
import ua.chnu.kmp.resources.lang_uk
import ua.chnu.kmp.resources.settings_footer
import ua.chnu.kmp.resources.settings_language
import ua.chnu.kmp.resources.settings_notifications
import ua.chnu.kmp.resources.settings_onboarding
import ua.chnu.kmp.resources.settings_theme
import ua.chnu.kmp.resources.settings_title
import ua.chnu.kmp.resources.theme_dark
import ua.chnu.kmp.resources.theme_light
import ua.chnu.kmp.resources.theme_system

private val ThemeMode.label: StringResource
    get() = when (this) {
        ThemeMode.DARK -> Res.string.theme_dark
        ThemeMode.LIGHT -> Res.string.theme_light
        ThemeMode.SYSTEM -> Res.string.theme_system
    }

private val AppLanguage.displayName: StringResource
    get() = when (this) {
        AppLanguage.UKRAINIAN -> Res.string.lang_uk
        AppLanguage.ENGLISH -> Res.string.lang_en
    }

/** User settings, persisted with multiplatform-settings. */
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = koinViewModel()) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val currentLanguage = effectiveLanguage(preferences.language)
    val colors = RetroTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        Text(
            text = stringResource(Res.string.settings_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )

        SettingsSection(Res.string.settings_language) {
            Row(Modifier.selectableGroup(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppLanguage.entries.forEach { language ->
                    val selected = language == currentLanguage
                    Keycap(
                        modifier = Modifier.weight(1f).height(93.dp),
                        onClick = { viewModel.onLanguageSelected(language) },
                        selected = selected,
                        role = Role.RadioButton,
                        color = if (selected) colors.rust else MaterialTheme.colorScheme.surface,
                        contentColor = if (selected) colors.onRust else MaterialTheme.colorScheme.onSurface,
                        contentAlignment = Alignment.TopStart,
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(language.shortLabel, style = MaterialTheme.typography.headlineMedium)
                            Text(stringResource(language.displayName), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                        }
                    }
                }
            }
        }

        SettingsSection(Res.string.settings_theme) {
            Row(Modifier.selectableGroup(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(ThemeMode.DARK, ThemeMode.LIGHT, ThemeMode.SYSTEM).forEach { mode ->
                    val selected = mode == preferences.themeMode
                    Keycap(
                        modifier = Modifier.weight(1f).height(50.dp),
                        onClick = { viewModel.onThemeSelected(mode) },
                        selected = selected,
                        role = Role.RadioButton,
                        color = if (selected) colors.inverse else MaterialTheme.colorScheme.surface,
                        contentColor = if (selected) colors.onInverse else MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(12.dp),
                        depth = 4.dp,
                    ) {
                        Text(stringResource(mode.label), style = MaterialTheme.typography.titleSmall)
                    }
                }
            }
        }

        Keycap(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            depth = 4.dp,
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = preferences.notificationsEnabled,
                            role = Role.Switch,
                            onValueChange = viewModel::onNotificationsChanged,
                        )
                        .height(60.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(Res.string.settings_notifications),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                    )
                    RetroSwitch(checked = preferences.notificationsEnabled, modifier = Modifier.size(width = 52.dp, height = 30.dp))
                }
                Box(Modifier.fillMaxWidth().height(2.dp).background(colors.outline))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(role = Role.Button, onClick = viewModel::onShowOnboardingAgain)
                        .height(60.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(Res.string.settings_onboarding),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(painterResource(Res.drawable.ic_chevron_right), contentDescription = null, modifier = Modifier.size(20.dp))
                }
            }
        }

        Text(
            text = stringResource(Res.string.settings_footer),
            style = MaterialTheme.typography.labelSmall,
            color = colors.muted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SettingsSection(title: StringResource, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(stringResource(title), style = MaterialTheme.typography.labelMedium, color = RetroTheme.colors.muted)
        content()
    }
}
