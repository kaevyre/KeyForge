package ua.chnu.kmp.presentation.login

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import ua.chnu.kmp.domain.auth.login.LoginUseCase
import ua.chnu.kmp.resources.Res
import ua.chnu.kmp.resources.error_password_invalid
import ua.chnu.kmp.resources.error_username_invalid

@Stable
class LoginViewModel internal constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val logger = Logger.withTag("Login")

    private val _state = MutableStateFlow(LoginState())
    internal val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _events = Channel<LoginEvent>(capacity = Channel.BUFFERED)
    val events: Flow<LoginEvent> = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnUsernameChange -> changeUsername(action.username)
            is LoginAction.OnPasswordChange -> changePassword(action.password)
            LoginAction.OnSignIn -> login()
        }
    }

    private fun changeUsername(username: String) {
        _state.update { it.copy(username = username, usernameError = null) }
    }

    private fun changePassword(password: String) {
        _state.update { it.copy(password = password, passwordError = null) }
    }

    private fun login() {
        val result = loginUseCase(_state.value.username, _state.value.password)
        _state.update {
            it.copy(
                usernameError = if (result.isUsernameValid) null else Res.string.error_username_invalid,
                passwordError = if (result.isPasswordValid) null else Res.string.error_password_invalid,
            )
        }

        if (result.isSuccess) {
            logger.i { "Signed in as ${_state.value.username}" }
            _events.trySend(LoginEvent.NavigateToMain)
        }
    }
}
