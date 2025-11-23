package com.custom.home.domain.usecase

import com.custom.core.repository.WeatherRepository
import com.custom.home.domain.model.WeatherUiModel
import com.custom.home.domain.toUiModel
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(city: String): Result<WeatherUiModel> {

        val result = weatherRepository.getCurrentWeatherByCity(city)

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