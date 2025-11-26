package com.custom.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.custom.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _navState = MutableStateFlow<NavState>(NavState.Loading)
    val navState: StateFlow<NavState> = _navState.asStateFlow()

    init {
        viewModelScope.launch {
            onboardingRepository.hasUserSeenOnboarding().collect { seen ->
                _navState.value = if (seen) NavState.Home else NavState.Onboarding
            }
        }
    }
}