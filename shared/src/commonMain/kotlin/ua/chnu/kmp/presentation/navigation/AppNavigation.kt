package ua.chnu.kmp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import ua.chnu.kmp.presentation.login.LoginScreen
import ua.chnu.kmp.presentation.main.MainScreen
import ua.chnu.kmp.presentation.onboarding.OnboardingScreen

private val navigationConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route.Main::class, Route.Main.serializer())
            subclass(Route.Onboarding::class, Route.Onboarding.serializer())
            subclass(Route.Login::class, Route.Login.serializer())
        }
    }
}

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(navigationConfig, Route.Main)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.Main> {
                MainScreen(
                    onNavigateToOnboarding = { backStack.replaceWith(Route.Onboarding) },
                )
            }
            entry<Route.Onboarding> {
                OnboardingScreen(
                    onNavigateToLogin = { backStack.replaceWith(Route.Login) },
                )
            }
            entry<Route.Login> {
                LoginScreen(
                    onNavigateToMain = { backStack.replaceWith(Route.Main) },
                )
            }
        },
    )
}

private fun NavBackStack<NavKey>.replaceWith(route: Route) {
    clear()
    add(route)
}
