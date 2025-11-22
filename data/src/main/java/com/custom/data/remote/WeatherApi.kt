package com.custom.data.remote

import com.custom.data.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String = "metric"
    ) : WeatherDto
}