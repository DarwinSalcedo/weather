package com.custom.weather

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.custom.home.ui.WeatherScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

       NavHost(
        navController = navController,
        startDestination = Destinations.WEATHER_ROUTE,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Destinations.WEATHER_ROUTE) {
            WeatherScreen()
        }
    }
}

object Destinations {
    const val WEATHER_ROUTE = "weather"
}