package com.custom.home.domain.usecase

import com.custom.domain.common.OperationResult
import com.custom.domain.repository.ErrorTranslator
import com.custom.domain.repository.WeatherRepository
import com.custom.home.domain.mapper.toUiModel
import com.custom.home.domain.model.WeatherUiModel
import javax.inject.Inject

class GetCurrentWeatherByCoordinateUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val exceptionTranslator: ErrorTranslator
) {

    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
    ): OperationResult<WeatherUiModel> {

        val result = weatherRepository.getCurrentWeatherByCoordinates(
            latitude,
            longitude
        )

        return result.fold(
            onSuccess = { domainModel ->
                OperationResult.Success(domainModel.toUiModel())
            },
            onFailure = { error ->
                OperationResult.Failure(exceptionTranslator.map(error))
            }
        )
    }
}