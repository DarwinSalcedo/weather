package com.custom.data.repository

import com.custom.data.mapper.toDomainModel
import com.custom.data.remote.WeatherApi
import com.custom.domain.model.WeatherModel
import com.custom.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: WeatherApi) : WeatherRepository {

    override suspend fun getCurrentWeatherByCity(city: String): Result<WeatherModel> {
        return try {

            val dto = api.getCurrentWeatherByCity(city)
            val domainModel = dto.toDomainModel()
            Result.success(domainModel)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getCurrentWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Result<WeatherModel> {
        return try {

            val dto = api.getCurrentWeatherByCoordinates(latitude, longitude)
            val domainModel = dto.toDomainModel()
            Result.success(domainModel)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}