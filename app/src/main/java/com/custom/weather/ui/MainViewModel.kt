package com.custom.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.custom.domain.repository.LanguageRepository
import com.custom.domain.repository.OnboardingRepository
import com.custom.weather.ui.NavState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    private val languageRepository: LanguageRepository,
) : ViewModel() {

    private val _languageCode = MutableStateFlow<String?>(null)
    val languageCode: StateFlow<String?> = _languageCode.asStateFlow()

    private val _navState = MutableStateFlow<NavState>(NavState.Loading)
    val navState: StateFlow<NavState> = _navState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            languageRepository.hasInitialLanguageBeenSet().collect { language ->
                _languageCode.value = language
                if (language == null) {
                    _navState.value = NavState.Language
                } else {
                    onboardingRepository.hasUserSeenOnboarding().collect { seen ->
                        _navState.value = if (seen) NavState.Home else NavState.Onboarding
                    }
                }
            }
        }
    }
}