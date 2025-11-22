package com.custom.data

import com.custom.data.dto.MainDto
import com.custom.data.dto.WeatherDetailDto
import com.custom.data.dto.WeatherDto
import com.custom.data.dto.WindDto
import com.custom.data.remote.WeatherApi
import com.custom.data.repository.WeatherRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException


class WeatherRepositoryTest {

    private val apiService: WeatherApi = mockk()
    private lateinit var repository: WeatherRepositoryImpl

    private val mockWeatherDto = WeatherDto(
        cityName = "Catamarca",
        main = MainDto(
            temperature = 25.5,
            tempMin = 20.0,
            tempMax = 30.0,
            humidity = 50
        ),
        weather = listOf(WeatherDetailDto("clear sky", "01d")),
        wind = WindDto(speed = 5.0)
    )

    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl(apiService)
    }

    @Test
    fun `Given a getCurrentWeather api call when the getCurrentWeatherByCity is invoke then the response is success`() =
        runTest {
            coEvery { apiService.getCurrentWeather("Catamarca") } returns mockWeatherDto

            val result = repository.getCurrentWeatherByCity("Catamarca")

            assertTrue(result.isSuccess)

            val weatherModel = result.getOrThrow()
            assertTrue(weatherModel.cityName == "Catamarca")
            assertTrue(weatherModel.temperatureModel.temperature == 25.5)
        }

    @Test
    fun `Given a getCurrentWeather api call when the getCurrentWeatherByCity is invoke then the response is failure`() =
        runTest {
            coEvery { apiService.getCurrentWeather(any()) } throws IOException("No connection")

            val result = repository.getCurrentWeatherByCity("London")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is IOException)
        }
}