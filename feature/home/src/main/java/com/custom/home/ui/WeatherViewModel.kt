package com.custom.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.custom.core.repository.LocationRepository
import com.custom.home.domain.model.TemperatureUiModel
import com.custom.home.domain.model.WeatherUiModel
import com.custom.home.domain.usecase.GetCurrentWeatherByCoordinateUseCase
import com.custom.home.domain.usecase.GetCurrentWeatherUseCase
import com.custom.home.domain.usecase.SearchCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MINIMIMUM_CHARACTERS_TO_SEARCH = 3

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getCurrentWeatherByCoordinateUseCase: GetCurrentWeatherByCoordinateUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase
) : ViewModel() {

    private val _weatherState =
        MutableStateFlow<WeatherState>(WeatherState.LocationPermissionRequired)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Init)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private var searchJob: Job? = null


    fun fetchWeatherByLocation() {
        if (_weatherState.value !is WeatherState.Success) {
            _weatherState.update { WeatherState.LoadingLocation }
        }

        viewModelScope.launch {
            locationRepository.getLocationUpdates()
                .collect { locationModel ->
                    fetchWeatherByCoordinates(locationModel.latitude, locationModel.longitude)
                    this.coroutineContext[Job]?.cancel()
                }
        }
    }


    fun fetchWeatherByCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _weatherState.update { WeatherState.LoadingWeather }

            val result =
                getCurrentWeatherByCoordinateUseCase(
                    latitude,
                    longitude
                )

            _weatherState.update {
                result.fold(
                    onSuccess = { weatherUiModel ->
                        WeatherState.Success(weatherUiModel)
                    },
                    onFailure = { e -> WeatherState.Error("Unexpected error loading the weather: ${e.message}") }
                )
            }
        }
    }


    fun handlePermissionSkipped() {
        val defaultWeatherModel = WeatherUiModel(
            cityName = "---",
            temperature = TemperatureUiModel("?", ""),
            conditionDescription = "---",
            humidityText = "-",
            windSpeedText = "-",
            iconUrl = ""
        )

        _weatherState.update { WeatherState.Success(defaultWeatherModel) }
    }


    fun refreshWeather() {
        _weatherState.update { WeatherState.LocationPermissionRequired }
    }



    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()

        if (query.isBlank() || query.length < MINIMIMUM_CHARACTERS_TO_SEARCH) {
            if (_searchState.value !is SearchState.Init) {
                _searchState.update { SearchState.Results(emptyList()) }
            }
            return
        }

        _searchState.update { SearchState.Searching }
        searchJob = viewModelScope.launch {

            val result = searchCitiesUseCase(query)

            _searchState.update {
                result.fold(
                    onSuccess = { citiesUiModel ->
                        SearchState.Results(citiesUiModel)
                    },
                    onFailure = { e -> SearchState.Error("Error searching ${e.message}") }
                )
            }
        }
    }


    fun activateSearch() {
        _searchState.update { SearchState.Results(emptyList()) }
    }


    fun clearSearch() {
        searchJob?.cancel()
        _searchState.update { SearchState.Init }
    }

    fun setPermissionRequired() {
        _weatherState.update {
            WeatherState.LocationPermissionRequired
        }
    }

    fun fetchWeatherByCity(city: String) {
        viewModelScope.launch {
            _weatherState.update { WeatherState.LoadingWeather }

            val result = getCurrentWeatherUseCase(city)
            _weatherState.update {
                result.fold(
                    onSuccess = { weatherUiModel ->
                        WeatherState.Success(weatherUiModel)
                    },
                    onFailure = { e -> WeatherState.Error("Error loading the weather for $city: ${e.message}") }
                )
            }
        }
    }
}