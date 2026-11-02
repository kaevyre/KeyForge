package ua.chnu.kmp.domain.auth.login

import ua.chnu.kmp.domain.auth.validation.ValidatePasswordUseCase
import ua.chnu.kmp.domain.auth.validation.ValidateUsernameUseCase

internal class LoginUseCase(
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
) {

    operator fun invoke(username: String, password: String): LoginResult {
        return LoginResult(
            isUsernameValid = validateUsernameUseCase(username),
            isPasswordValid = validatePasswordUseCase(password),
        )
    }
}
