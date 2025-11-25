package com.custom.home.usecase

import com.custom.core.model.TemperatureModel
import com.custom.core.model.WeatherModel
import com.custom.core.repository.WeatherRepository
import com.custom.core.util.AppError
import com.custom.core.util.OperationResult
import com.custom.home.domain.toUiModel
import com.custom.home.domain.usecase.GetCurrentWeatherByCoordinateUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetCurrentWeatherByCoordinateUseCaseTest {

    private val mockWeatherRepository: WeatherRepository = mockk()
    private lateinit var useCase: GetCurrentWeatherByCoordinateUseCase

    private val LAT = -34.6037
    private val LON = -58.3816
    private val CITY_NAME = "Buenos Aires"

    private val domainWeatherModel = WeatherModel(
        cityName = CITY_NAME,
        temperatureModel = TemperatureModel(
            temperature = 25.0,
            minimumTemperature = 18.0,
            maximumTemperature = 32.0
        ),
        conditionDescription = "clear sky",
        humidityPercentage = 75,
        windSpeed = 10.5,
        iconUrl = "http://mock.url/01d.png"
    )

    private val expectedUiModel = domainWeatherModel.toUiModel()


    @Before
    fun setUp() {
        useCase = GetCurrentWeatherByCoordinateUseCase(mockWeatherRepository)
    }

    @Test
    fun `Given a successful fetch, when use case is invoked then  should return Success with mapped WeatherUiModel on successful fetch`() =
        runTest {
            coEvery {
                mockWeatherRepository.getCurrentWeatherByCoordinates(LAT, LON)
            } returns Result.success(domainWeatherModel)

            val result = useCase.invoke(LAT, LON)

            Assert.assertTrue(result is OperationResult.Success)
            val actualUiModel = (result as (OperationResult.Success)).data

            Assert.assertTrue(expectedUiModel.cityName == actualUiModel.cityName)
            Assert.assertTrue(
                expectedUiModel.currentTemperature ==
                        actualUiModel.currentTemperature
            )
            Assert.assertTrue(
                expectedUiModel.windSpeedText ==
                actualUiModel.windSpeedText
            )
        }

    @Test
    fun `Given a failed fetch, when use case is invoked then it should return Failure result on repository exception`() =
        runTest {
            val mockException = IOException("Network Error")

            coEvery {
                mockWeatherRepository.getCurrentWeatherByCoordinates(LAT, LON)
            } returns Result.failure(mockException)

            val result = useCase.invoke(LAT, LON)

            Assert.assertTrue(result is OperationResult.Failure)
            Assert.assertEquals(
                OperationResult.Failure(AppError.NetworkError),
                result
            )
        }
}