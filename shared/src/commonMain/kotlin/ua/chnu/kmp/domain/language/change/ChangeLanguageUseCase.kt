package ua.chnu.kmp.domain.language.change

import ua.chnu.kmp.domain.settings.PreferencesRepository
import ua.chnu.kmp.domain.settings.model.AppLanguage

internal class ChangeLanguageUseCase(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(language: AppLanguage) {
        preferencesRepository.setLanguage(language)
    }
}
