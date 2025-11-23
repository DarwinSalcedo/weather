package com.custom.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase
) : ViewModel() {

    private val _weatherState =
        MutableStateFlow<WeatherState>(WeatherState.LocationPermissionRequired)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Init)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private var searchJob: Job? = null


    fun refreshWeather() {
        _weatherState.update { WeatherState.LocationPermissionRequired }
    }


    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()

        if (query.isBlank() || query.length < 3) {
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
                    onFailure = { e -> SearchState.Error("Error ${e.message}") }
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


    fun fetchWeatherByCity(city: String) {
        viewModelScope.launch {
            _weatherState.update { WeatherState.LoadingWeather }

            val result = getCurrentWeatherUseCase(city)
            _weatherState.update {
                result.fold(
                    onSuccess = { weatherUiModel ->
                        WeatherState.Success(weatherUiModel)
                    },
                    onFailure = { e -> WeatherState.Error("Error $city: ${e.message}") }
                )
            }
        }
    }
}