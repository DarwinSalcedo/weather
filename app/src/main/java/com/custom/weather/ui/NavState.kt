package com.custom.weather.ui

sealed class NavState {
    data object Loading : NavState()
    data object Language : NavState()
    data object Onboarding : NavState()
    data object Home : NavState()
}