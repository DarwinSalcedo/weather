package com.custom.home.domain.model

data class WeatherUiModel(
    val cityName: String,
    val temperature: TemperatureUiModel,
    val conditionDescription: String,
    val humidityText: String,
    val windSpeedText: String,
    val iconUrl: String
)