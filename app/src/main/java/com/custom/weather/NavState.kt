package com.custom.weather

sealed class NavState {
    data object Loading : NavState()
    data object Onboarding : NavState()
    data object Home : NavState()
}