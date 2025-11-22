package com.custom.core.model


data class WeatherModel(
    val cityName: String,
    val temperatureModel: TemperatureModel,
    val conditionDescription: String,
    val humidityPercentage: Int,
    val windSpeed: Double,
    val iconUrl: String
)

