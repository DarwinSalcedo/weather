package com.custom.home.ui

import com.custom.domain.common.AppError
import com.custom.home.domain.model.WeatherUiModel

sealed class WeatherState {
    data object Init : WeatherState()

    data object LocationPermissionRequired : WeatherState()

    data object LoadingLocation : WeatherState()

    data object LoadingWeather : WeatherState()

    data class Success(val weather: WeatherUiModel) : WeatherState()

    data class Error(val error: AppError) : WeatherState()
}