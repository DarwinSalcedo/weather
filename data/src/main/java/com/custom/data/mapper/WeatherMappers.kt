package com.custom.data.mapper

import com.custom.data.BuildConfig
import com.custom.data.dto.WeatherDto
import com.custom.domain.model.TemperatureModel
import com.custom.domain.model.WeatherModel


fun WeatherDto.toDomainModel(): WeatherModel {

    val weatherDetail = this.weather.firstOrNull()

    val temperature = TemperatureModel(
        temperature = this.main.temperature,
        minimumTemperature = this.main.tempMin,
        maximumTemperature = this.main.tempMax
    )

    return WeatherModel(
        cityName = this.cityName,
        temperatureModel = temperature,
        conditionDescription = weatherDetail?.description.orEmpty(),
        humidityPercentage = this.main.humidity,
        windSpeed = this.wind.speed,
        iconUrl = "${BuildConfig.ICON_BASE_URL}${weatherDetail?.iconCode}@2x.png"
    )
}