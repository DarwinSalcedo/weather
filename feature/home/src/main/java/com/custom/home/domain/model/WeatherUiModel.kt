package com.custom.home.domain.model


data class WeatherUiModel(
    val isPlaceholder: Boolean = false,
    val cityName: String,
    val temperature: TemperatureUiModel,
    val conditionDescription: String,
    val humidityText: String,
    val windSpeedText: String,
    val iconUrl: String
) {
    companion object {
        val default = WeatherUiModel(
            isPlaceholder = true,
            cityName = "",
            temperature = TemperatureUiModel("", ""),
            conditionDescription = "",
            humidityText = "",
            windSpeedText = "",
            iconUrl = ""
        )
    }
}