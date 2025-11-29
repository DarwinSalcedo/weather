package com.custom.weather.ui.navigation

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.custom.common.components.LoadingScreen
import com.custom.home.ui.WeatherScreen
import com.custom.language.ui.LanguageSelectionScreen
import com.custom.onboarding.ui.OnboardingScreen
import com.custom.weather.ui.MainViewModel
import com.custom.weather.ui.NavState
import com.custom.weather.R


@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val navState by mainViewModel.navState.collectAsState()

    val languageCode by mainViewModel.languageCode.collectAsState()


    val startDestination = when (navState) {
        NavState.Loading -> null
        NavState.Onboarding -> Destinations.ONBOARDING_ROUTE
        NavState.Home -> Destinations.WEATHER_ROUTE
        NavState.Language -> Destinations.LANGUAGE_ROUTE
    }

    LaunchedEffect(languageCode) {
        if (!languageCode.isNullOrEmpty())
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(
                    languageCode
                )
            )
    }

    if (startDestination != null) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize()
        ) {

            composable(Destinations.LANGUAGE_ROUTE) {
                LanguageSelectionScreen(
                    onLanguageSelected = {
                        navController.navigate(Destinations.ONBOARDING_ROUTE) {
                            popUpTo(Destinations.LANGUAGE_ROUTE) { inclusive = true }
                        }
                    }
                )
            }

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
    const val LANGUAGE_ROUTE = "language_selection"
    const val ONBOARDING_ROUTE = "onboarding"
    const val WEATHER_ROUTE = "weather"
}
