package ua.chnu.kmp.di

import co.touchlab.kermit.Logger as KermitLogger
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ua.chnu.kmp.data.cart.InMemoryCartRepository
import ua.chnu.kmp.data.catalog.LocalProductRepository
import ua.chnu.kmp.data.settings.AppPreferencesRepository
import ua.chnu.kmp.domain.auth.login.LoginUseCase
import ua.chnu.kmp.domain.auth.validation.ValidatePasswordUseCase
import ua.chnu.kmp.domain.auth.validation.ValidateUsernameUseCase
import ua.chnu.kmp.domain.cart.CartRepository
import ua.chnu.kmp.domain.catalog.ProductRepository
import ua.chnu.kmp.domain.catalog.usecase.GetProductsUseCase
import ua.chnu.kmp.domain.language.change.ChangeLanguageUseCase
import ua.chnu.kmp.domain.language.obtain.ObtainLanguageUseCase
import ua.chnu.kmp.domain.onboarding.check.CheckOnboardingUseCase
import ua.chnu.kmp.domain.onboarding.complete.CompleteOnboardingUseCase
import ua.chnu.kmp.domain.settings.PreferencesRepository
import ua.chnu.kmp.presentation.RootViewModel
import ua.chnu.kmp.presentation.builder.BuilderViewModel
import ua.chnu.kmp.presentation.cart.CartViewModel
import ua.chnu.kmp.presentation.catalog.CatalogViewModel
import ua.chnu.kmp.presentation.login.LoginViewModel
import ua.chnu.kmp.presentation.main.MainViewModel
import ua.chnu.kmp.presentation.onboarding.OnboardingViewModel
import ua.chnu.kmp.presentation.settings.SettingsViewModel

val networkModule = module {
    single {
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    private val log = KermitLogger.withTag("Ktor")
                    override fun log(message: String) {
                        log.d { message }
                    }
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}
val domainModule = module {
    factoryOf(::GetProductsUseCase)
    factoryOf(::CompleteOnboardingUseCase)
    factoryOf(::CheckOnboardingUseCase)
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::ChangeLanguageUseCase)
    factoryOf(::ObtainLanguageUseCase)
}

val dataModule = module {
    single<Settings> { Settings() }
    singleOf(::AppPreferencesRepository) { bind<PreferencesRepository>() }
    singleOf(::LocalProductRepository) { bind<ProductRepository>() }
    singleOf(::InMemoryCartRepository) { bind<CartRepository>() }
}

val appModule = module {
    includes(networkModule, domainModule, dataModule)
    viewModelOf(::RootViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::CatalogViewModel)
    viewModelOf(::BuilderViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::SettingsViewModel)
}
