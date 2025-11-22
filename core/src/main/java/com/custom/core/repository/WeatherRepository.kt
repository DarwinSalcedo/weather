package com.custom.core.repository

import com.custom.core.model.WeatherModel

interface WeatherRepository {

    /**
     * Get the current weather of a city.
     * @param city the name of the city.
     * @return Result<WeatherModel>.
     */
    suspend fun getCurrentWeatherByCity(city: String): Result<WeatherModel>

}