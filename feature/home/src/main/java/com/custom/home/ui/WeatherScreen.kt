package com.custom.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.custom.home.components.WeatherContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val context = LocalContext.current



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("City forecast") },
                actions = {
                    val isReadyForRefresh =
                        weatherState is WeatherState.Success || weatherState is WeatherState.Error

                    if (isReadyForRefresh && weatherState !is WeatherState.LoadingWeather && weatherState !is WeatherState.LoadingLocation) {
                        IconButton(onClick = viewModel::refreshWeather) {
                            Icon(
                                Icons.Default.Place,
                                contentDescription = "Refresh local weather"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            when (val state = weatherState) {
                is WeatherState.LocationPermissionRequired -> {
                    //todo
                }

                is WeatherState.LoadingLocation, WeatherState.LoadingWeather -> {
                    LoadingScreen(
                        message = if (state is WeatherState.LoadingLocation)
                            "Getting location"
                        else
                            "Updating weather"
                    )
                }

                is WeatherState.Success -> {
                    WeatherContent(
                        weather = state.weather,
                        onSearchClicked = viewModel::activateSearch
                    )
                }

                is WeatherState.Error -> {
                    ErrorScreen(message = state.message)
                }

                WeatherState.Init -> Unit
            }

        }
    }
}