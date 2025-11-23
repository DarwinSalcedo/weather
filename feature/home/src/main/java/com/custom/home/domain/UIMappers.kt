package com.custom.home.domain

import com.custom.core.model.CityModel
import com.custom.core.model.WeatherModel
import com.custom.home.domain.model.CityUiModel
import com.custom.home.domain.model.TemperatureUiModel
import com.custom.home.domain.model.WeatherUiModel

fun WeatherModel.toUiModel(): WeatherUiModel {
    val tempUiModel = TemperatureUiModel(
        current = "${this.temperatureModel.temperature.toInt()} °C",
        minMax = "Mín: ${this.temperatureModel.minimumTemperature.toInt()} °C / Máx: ${this.temperatureModel.maximumTemperature.toInt()} °C",
    )

    return WeatherUiModel(
        cityName = this.cityName,
        temperature = tempUiModel,
        conditionDescription = this.conditionDescription.replaceFirstChar { it.uppercase() },
        humidityText = "${this.humidityPercentage}%",
        windSpeedText = "${
            String.format(
                "%.1f",
                this.windSpeed
            )
        } m/s",
        iconUrl = this.iconUrl
    )
}

fun CityModel.toUiModel(): CityUiModel {
    return CityUiModel(
        name = this.name,
        country = this.country,
        latitude = this.latitude,
        longitude = this.longitude,
        coordinatesText = "Lat: ${
            String.format(
                "%.2f",
                this.latitude
            )
        }, Lon: ${String.format("%.2f", this.longitude)}",
    )
}