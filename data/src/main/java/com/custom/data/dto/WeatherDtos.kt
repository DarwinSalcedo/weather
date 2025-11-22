package com.custom.data.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("name") val cityName: String,
    @SerializedName("main") val main: MainDto,
    @SerializedName("weather") val weather: List<WeatherDetailDto>,
    @SerializedName("wind") val wind: WindDto
)

data class MainDto(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("humidity") val humidity: Int
)

data class WeatherDetailDto(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val iconCode: String
)

data class WindDto(
    @SerializedName("speed") val speed: Double
)