package ua.chnu.kmp.presentation.login

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
internal data class LoginState(
    val username: String = "",
    val password: String = "",
    val usernameError: StringResource? = null,
    val passwordError: StringResource? = null,
)
