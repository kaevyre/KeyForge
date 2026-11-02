package ua.chnu.kmp.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Main : Route

    @Serializable
    data object Onboarding : Route

    @Serializable
    data object Login : Route
}
