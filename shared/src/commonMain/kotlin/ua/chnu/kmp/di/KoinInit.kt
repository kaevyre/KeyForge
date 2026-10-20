package ua.chnu.kmp.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    Logger.setTag("KeyForge")
    Logger.setMinSeverity(Severity.Debug)
    Logger.i { "Starting KeyForge" }
    return startKoin {
        includes(config)
        modules(appModule)
    }
}
