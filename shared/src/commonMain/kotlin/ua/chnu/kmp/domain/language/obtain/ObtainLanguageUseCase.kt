package ua.chnu.kmp.domain.language.obtain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ua.chnu.kmp.domain.settings.PreferencesRepository
import ua.chnu.kmp.domain.settings.model.AppLanguage

internal class ObtainLanguageUseCase(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(): Flow<AppLanguage?> {
        return preferencesRepository.preferences
            .map { it.language }
            .distinctUntilChanged()
    }
}
