package com.custom.domain.repository

import com.custom.domain.model.WeatherModel

interface WeatherRepository {

    /**
     * Get the current weather of a city.
     * @param city the name of the city.
     * @return Result<WeatherModel>.
     */
    suspend fun getCurrentWeatherByCity(city: String): Result<WeatherModel>

    /**
     * Get the current weather of a city.
     * @param latitude the latitude of the city.
     * @param longitude the longitude of the city.
     */
    suspend fun getCurrentWeatherByCoordinates(
        latitude: Double,
        longitude: Double,
    ): Result<WeatherModel>


}