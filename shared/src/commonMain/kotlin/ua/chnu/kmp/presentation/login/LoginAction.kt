package ua.chnu.kmp.presentation.login

sealed class LoginAction {
    data class OnUsernameChange(val username: String) : LoginAction()
    data class OnPasswordChange(val password: String) : LoginAction()
    data object OnSignIn : LoginAction()
}
