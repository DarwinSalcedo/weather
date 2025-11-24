package com.custom.home.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.custom.core.model.LocationModel
import com.custom.core.repository.LocationRepository
import com.custom.home.domain.model.CityUiModel
import com.custom.home.domain.model.TemperatureUiModel
import com.custom.home.domain.model.WeatherUiModel
import com.custom.home.domain.usecase.GetCurrentWeatherByCoordinateUseCase
import com.custom.home.domain.usecase.SearchCitiesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var mockLocationRepository: LocationRepository

    @RelaxedMockK
    private lateinit var mockWeatherUseCase: GetCurrentWeatherByCoordinateUseCase

    @RelaxedMockK
    private lateinit var mockSearchUseCase: SearchCitiesUseCase

    private lateinit var viewModel: WeatherViewModel

    private val MOCK_LAT = 10.0
    private val MOCK_LON = 20.0
    private val MOCK_CITY_NAME = "London"
    private val MOCK_WEATHER_UI = WeatherUiModel(
        cityName = MOCK_CITY_NAME,
        temperature = TemperatureUiModel("25°C", "18°C/32°C"),
        conditionDescription = "Clear",
        humidityText = "50%",
        windSpeedText = "10.5 m/s",
        iconUrl = "url"
    )
    private val MOCK_CITIES_UI = listOf(CityUiModel(MOCK_CITY_NAME, "", 0.0, 0.0, ""))

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = WeatherViewModel(
            mockLocationRepository,
            mockWeatherUseCase,
            mockSearchUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `Given a location  when the fetchWeatherByCoordinates it's invoked then check the order and return a success`() =
        runTest {
            val locationModel = LocationModel(MOCK_LAT, MOCK_LON)

            every { mockLocationRepository.getLocationUpdates() } returns flowOf(locationModel)

            coEvery {
                mockWeatherUseCase(MOCK_LAT, MOCK_LON)
            } returns Result.success(MOCK_WEATHER_UI)


            viewModel.fetchWeatherByLocation()
            advanceUntilIdle()

            coVerifyOrder {
                mockLocationRepository.getLocationUpdates()
                mockWeatherUseCase(MOCK_LAT, MOCK_LON)
            }

            assertTrue(viewModel.weatherState.value is WeatherState.Success)

        }


    @Test
    fun `Given a coordinate when fetchWeatherByCoordinates is called, then it  return a successful response`() =
        runTest {
            coEvery {
                mockWeatherUseCase(MOCK_LAT, MOCK_LON)
            } returns Result.success(MOCK_WEATHER_UI)

            viewModel.fetchWeatherByCoordinates(MOCK_LAT, MOCK_LON)

            advanceUntilIdle()

            assertTrue(viewModel.weatherState.value is WeatherState.Success)
            val successState = viewModel.weatherState.value as WeatherState.Success
            assertEquals(MOCK_CITY_NAME, successState.weather.cityName)
            assertFalse(successState.weather.isPlaceholder)
        }

    @Test
    fun `Given an Api Error, when fetchWeatherByCoordinates is called, then it  returns a result error `() =
        runTest {
            coEvery {
                mockWeatherUseCase(any(), any())
            } returns Result.failure(Exception("API error"))

            viewModel.fetchWeatherByCoordinates(MOCK_LAT, MOCK_LON)

            advanceUntilIdle()

            assertTrue(viewModel.weatherState.value is WeatherState.Error)
        }


    @Test
    fun `Given short query to search, when onSearchQueryChanged is called then, it should return empty list`() =
        runTest {
            val shortQuery = "PA"

            coEvery { mockSearchUseCase(shortQuery) } returns Result.success(emptyList())

            viewModel.onSearchQueryChanged(shortQuery)
            advanceUntilIdle()

            assertTrue(viewModel.searchState.value is SearchState.Results)
            val resultsState = viewModel.searchState.value as SearchState.Results
            assertTrue(resultsState.cities.isEmpty())

        }


    @Test
    fun `Given a long query to search, when onSearchQueryChanged is called, it returns a valid result`() =
        runTest {
            val query = "London"

            coEvery { mockSearchUseCase(query) } returns Result.success(MOCK_CITIES_UI)

            viewModel.onSearchQueryChanged(query)
            advanceUntilIdle()

            assertTrue(viewModel.searchState.value is SearchState.Results)
            val resultsState = viewModel.searchState.value as SearchState.Results
            assertEquals(1, resultsState.cities.size)
            assertEquals(MOCK_CITY_NAME, resultsState.cities.first().name)
        }

    @Test
    fun `Given two queries to search, when onSearchQueryChanged is called, then it cancels previous job and returns a valid result`() =
        runTest {

            coEvery { mockSearchUseCase("A") } coAnswers { throw Exception("prevous job canceled") }

            coEvery { mockSearchUseCase("AB") } returns Result.success(MOCK_CITIES_UI)

            viewModel.onSearchQueryChanged("A")

            viewModel.onSearchQueryChanged("AB")
            advanceUntilIdle()

            assertTrue(viewModel.searchState.value is SearchState.Results)
        }


    @Test
    fun `When activateSearch is called, then it must update searchState to Results(empty)`() {
        viewModel.activateSearch()
        assertTrue(viewModel.searchState.value is SearchState.Results)
    }

    @Test
    fun `When clearSearch is called then they must update searchState to Init`() {
        viewModel.clearSearch()
        assertEquals(SearchState.Init, viewModel.searchState.value)
    }

    @Test
    fun `When  a handlePermissionSkipped is called, then it has to set WeatherState to Success with default placeholder`() {
        viewModel.handlePermissionSkipped()

        assertTrue(viewModel.weatherState.value is WeatherState.Success)
        val successState = viewModel.weatherState.value as WeatherState.Success
        assertTrue(successState.weather.isPlaceholder)
    }
}