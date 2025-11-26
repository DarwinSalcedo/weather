package com.custom.weather

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.custom.common.components.LoadingScreen
import com.custom.home.ui.WeatherScreen
import com.custom.onboarding.ui.OnboardingScreen


@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val navState by mainViewModel.navState.collectAsState()


    val startDestination = when (navState) {
        NavState.Loading -> null
        NavState.Onboarding -> Destinations.ONBOARDING_ROUTE
        NavState.Home -> Destinations.WEATHER_ROUTE
    }

    if (startDestination != null) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Destinations.ONBOARDING_ROUTE) {
                OnboardingScreen(
                    onOnboardingComplete = {
                        navController.navigate(Destinations.WEATHER_ROUTE) {
                            popUpTo(Destinations.ONBOARDING_ROUTE) { inclusive = true }
                        }
                    }
                )
            }

            composable(Destinations.WEATHER_ROUTE) {
                WeatherScreen()
            }
        }
    } else {
        LoadingScreen(stringResource(R.string.loading_resources))
    }
}


object Destinations {
    const val ONBOARDING_ROUTE = "onboarding"
    const val WEATHER_ROUTE = "weather"
}