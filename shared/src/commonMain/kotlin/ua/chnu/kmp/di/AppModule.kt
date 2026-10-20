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
import ua.chnu.kmp.data.posts.AppPostApiService
import ua.chnu.kmp.data.posts.AppPostRepository
import ua.chnu.kmp.data.posts.PostApiService
import ua.chnu.kmp.data.settings.AppPreferencesRepository
import ua.chnu.kmp.domain.cart.CartRepository
import ua.chnu.kmp.domain.catalog.ProductRepository
import ua.chnu.kmp.domain.catalog.usecase.GetProductsUseCase
import ua.chnu.kmp.domain.posts.PostRepository
import ua.chnu.kmp.domain.posts.usecase.CreatePostUseCase
import ua.chnu.kmp.domain.posts.usecase.DeletePostUseCase
import ua.chnu.kmp.domain.posts.usecase.GetPostsUseCase
import ua.chnu.kmp.domain.posts.usecase.UpdatePostUseCase
import ua.chnu.kmp.domain.settings.PreferencesRepository
import ua.chnu.kmp.presentation.RootViewModel
import ua.chnu.kmp.presentation.builder.BuilderViewModel
import ua.chnu.kmp.presentation.cart.CartViewModel
import ua.chnu.kmp.presentation.catalog.CatalogViewModel
import ua.chnu.kmp.presentation.main.MainViewModel
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

    singleOf(::AppPostApiService) { bind<PostApiService>() }
}
val domainModule = module {
    factoryOf(::GetPostsUseCase)
    factoryOf(::CreatePostUseCase)
    factoryOf(::UpdatePostUseCase)
    factoryOf(::DeletePostUseCase)
    factoryOf(::GetProductsUseCase)
}

val dataModule = module {
    // No-arg factory picks the platform store: SharedPreferences, NSUserDefaults, Preferences, localStorage.
    single<Settings> { Settings() }
    singleOf(::AppPreferencesRepository) { bind<PreferencesRepository>() }
    singleOf(::LocalProductRepository) { bind<ProductRepository>() }
    singleOf(::InMemoryCartRepository) { bind<CartRepository>() }
}

val appModule = module {
    includes(networkModule, domainModule, dataModule)
    singleOf(::AppPostRepository) { bind<PostRepository>() }
    viewModelOf(::RootViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::CatalogViewModel)
    viewModelOf(::BuilderViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::SettingsViewModel)
}
