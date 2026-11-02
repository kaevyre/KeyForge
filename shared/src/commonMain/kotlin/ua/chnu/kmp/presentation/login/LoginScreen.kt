package ua.chnu.kmp.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ua.chnu.kmp.presentation.theme.RetroTheme
import ua.chnu.kmp.presentation.ui.ObserveEvents
import ua.chnu.kmp.presentation.ui.components.Keycap
import ua.chnu.kmp.presentation.ui.components.dashedBorder
import ua.chnu.kmp.resources.Res
import ua.chnu.kmp.resources.login_password
import ua.chnu.kmp.resources.login_password_hint
import ua.chnu.kmp.resources.login_sign_in
import ua.chnu.kmp.resources.login_subtitle
import ua.chnu.kmp.resources.login_title
import ua.chnu.kmp.resources.login_username
import ua.chnu.kmp.resources.login_username_hint

@Composable
fun LoginScreen(
    onNavigateToMain: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            LoginEvent.NavigateToMain -> onNavigateToMain()
        }
    }

    LoginContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun LoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    val colors = RetroTheme.colors
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(Modifier.widthIn(max = 420.dp).fillMaxWidth()) {
            Text(
                text = buildAnnotatedString {
                    append("KEY")
                    withStyle(SpanStyle(color = colors.rust)) { append("FORGE") }
                },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(40.dp))
            Text(
                text = stringResource(Res.string.login_title),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(Res.string.login_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = colors.body,
            )
            Spacer(Modifier.height(32.dp))

            LoginField(
                label = Res.string.login_username,
                hint = Res.string.login_username_hint,
                value = state.username,
                error = state.usernameError,
                onValueChange = { onAction(LoginAction.OnUsernameChange(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
            )
            Spacer(Modifier.height(20.dp))
            LoginField(
                label = Res.string.login_password,
                hint = Res.string.login_password_hint,
                value = state.password,
                error = state.passwordError,
                onValueChange = { onAction(LoginAction.OnPasswordChange(it)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onAction(LoginAction.OnSignIn) },
                ),
            )

            Spacer(Modifier.height(32.dp))
            Keycap(
                modifier = Modifier.fillMaxWidth().height(61.dp),
                onClick = { onAction(LoginAction.OnSignIn) },
                color = colors.rust,
                contentColor = colors.onRust,
            ) {
                Text(
                    text = stringResource(Res.string.login_sign_in),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
private fun LoginField(
    label: StringResource,
    hint: StringResource,
    value: String,
    error: StringResource?,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val colors = RetroTheme.colors
    val labelText = stringResource(label)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = labelText, style = MaterialTheme.typography.labelMedium, color = colors.muted)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(colors.rust),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .dashedBorder(if (error != null) colors.rust else colors.outline, 2.dp, 12.dp)
                .semantics { contentDescription = labelText },
            decorationBox = { inner ->
                Box(Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(stringResource(hint), style = MaterialTheme.typography.bodyLarge, color = colors.muted)
                    }
                    inner()
                }
            },
        )
        if (error != null) {
            Text(text = stringResource(error), style = MaterialTheme.typography.bodySmall, color = colors.rust)
        }
    }
}
