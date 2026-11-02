package ua.chnu.kmp.presentation.login

sealed class LoginEvent {
    data object NavigateToMain : LoginEvent()
}
