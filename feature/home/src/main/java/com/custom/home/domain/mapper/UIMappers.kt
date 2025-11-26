package com.custom.home.domain.mapper

import com.custom.domain.model.CityModel
import com.custom.domain.model.WeatherModel
import com.custom.home.domain.model.CityUiModel
import com.custom.home.domain.model.WeatherUiModel

fun WeatherModel.toUiModel(): WeatherUiModel {

    return WeatherUiModel(
        cityName = this.cityName,
        currentTemperature = this.temperatureModel.temperature,
        minTemperature = this.temperatureModel.minimumTemperature,
        maxTemperature = this.temperatureModel.maximumTemperature,
        conditionDescription = this.conditionDescription.replaceFirstChar { it.uppercase() },
        humidityPercentage = this.humidityPercentage,
        windSpeedText = this.windSpeed,
        iconUrl = this.iconUrl
    )
}

fun CityModel.toUiModel(): CityUiModel {
    return CityUiModel(
        name = this.name,
        country = this.country,
        latitude = this.latitude,
        longitude = this.longitude
    )
}