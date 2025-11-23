package com.custom.home.domain.usecase

import com.custom.core.repository.WeatherRepository
import com.custom.home.domain.model.WeatherUiModel
import com.custom.home.domain.toUiModel
import javax.inject.Inject

class GetCurrentWeatherByCoordinateUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
    ): Result<WeatherUiModel> {

        val result = weatherRepository.getCurrentWeatherByCoordinates(
            latitude,
            longitude
        )

        return result.fold(
            onSuccess = { domainModel ->
                Result.success(domainModel.toUiModel())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}