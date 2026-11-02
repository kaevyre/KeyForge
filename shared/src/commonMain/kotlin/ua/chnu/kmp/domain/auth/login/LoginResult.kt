package ua.chnu.kmp.domain.auth.login

internal data class LoginResult(
    val isUsernameValid: Boolean,
    val isPasswordValid: Boolean,
) {
    val isSuccess: Boolean
        get() = isUsernameValid && isPasswordValid
}
