package ua.chnu.kmp.domain.auth.validation

private val USERNAME_REGEX = Regex("^[A-Za-z0-9_]{3,20}$")

internal class ValidateUsernameUseCase {

    operator fun invoke(username: String): Boolean {
        return USERNAME_REGEX.matches(username)
    }
}
