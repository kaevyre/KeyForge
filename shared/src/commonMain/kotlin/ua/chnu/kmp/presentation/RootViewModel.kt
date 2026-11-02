package ua.chnu.kmp.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import ua.chnu.kmp.domain.settings.PreferencesRepository
import ua.chnu.kmp.domain.settings.model.UserPreferences

class RootViewModel internal constructor(
    preferencesRepository: PreferencesRepository,
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = preferencesRepository.preferences
}
