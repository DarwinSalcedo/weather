package com.custom.home.domain.model


data class WeatherUiModel(
    val isPlaceholder: Boolean = false,
    val cityName: String,
    val currentTemperature: Double,
    val minTemperature: Double,
    val maxTemperature: Double,
    val conditionDescription: String,
    val humidityPercentage: Int,
    val windSpeedText: Double,
    val iconUrl: String
) {
    companion object {
        val default = WeatherUiModel(
            isPlaceholder = true,
            cityName = "",
            currentTemperature = 0.0,
            minTemperature = 0.0,
            maxTemperature = 0.0,
            conditionDescription = "",
            humidityPercentage = 0,
            windSpeedText = 0.0,
            iconUrl = ""
        )
    }
}