package com.custom.home.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.fonts.FontStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.custom.home.R
import com.custom.home.components.SearchOverlay
import com.custom.home.components.WeatherContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.fetchWeatherByLocation()
        } else {
            viewModel.handlePermissionSkipped()
        }
    }

    val hasLocationPermission = {
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    LaunchedEffect(weatherState) {
        if (weatherState is WeatherState.LocationPermissionRequired && hasLocationPermission()) {
            viewModel.fetchWeatherByLocation()
        } else if (weatherState is WeatherState.Init) {
            viewModel.setPermissionRequired()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.city_forecast), style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    val isReadyForRefresh =
                        weatherState is WeatherState.Success || weatherState is WeatherState.Error

                    if (isReadyForRefresh && weatherState !is WeatherState.LoadingWeather && weatherState !is WeatherState.LoadingLocation) {
                        IconButton(onClick = viewModel::refreshWeather) {
                            Icon(
                                Icons.Default.Place,
                                contentDescription = stringResource(R.string.refresh_local_weather)
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
                    if (!hasLocationPermission()) {
                        LocationPermissionScreen(
                            onPermissionGranted = {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            },
                            onPermissionSkipped = { viewModel.handlePermissionSkipped() }
                        )
                    }
                }

                is WeatherState.LoadingLocation, WeatherState.LoadingWeather -> {
                    LoadingScreen(
                        message = if (state is WeatherState.LoadingLocation)
                            stringResource(R.string.getting_location)
                        else
                            stringResource(R.string.updating_weather)
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

            SearchOverlay(
                searchState = searchState,
                onQueryChanged = viewModel::onSearchQueryChanged,
                onCitySelected = { city ->
                    viewModel.fetchWeatherByCoordinates(city.latitude,city.longitude)
                    viewModel.clearSearch()
                },
                onClose = viewModel::clearSearch
            )
        }
    }
}