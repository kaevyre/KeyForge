package ua.chnu.kmp.domain.auth.validation

private val PASSWORD_REGEX = Regex("""^(?=.*[A-Za-z])(?=.*\d)\S{6,}$""")

internal class ValidatePasswordUseCase {

    operator fun invoke(password: String): Boolean {
        return PASSWORD_REGEX.matches(password)
    }
}
