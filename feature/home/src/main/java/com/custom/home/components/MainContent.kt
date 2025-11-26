package com.custom.home.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.custom.home.R
import com.custom.home.domain.model.WeatherUiModel
import kotlin.math.roundToInt


@Composable
fun MainContent(weather: WeatherUiModel) {
    Text(
        weather.cityName,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )

    AsyncImage(
        model = weather.iconUrl,
        contentDescription = weather.conditionDescription,
        modifier = Modifier.size(150.dp)
    )

    Text(
        text = stringResource(
            R.string.current_temp,
            weather.currentTemperature.roundToInt()
        ),
        style = MaterialTheme.typography.displayLarge
    )

    Text(weather.conditionDescription, style = MaterialTheme.typography.headlineSmall)

    Spacer(Modifier.height(32.dp))

    HorizontalDivider(Modifier.fillMaxWidth(0.8f))

    DetailRow(
        stringResource(R.string.humidity_title),
        stringResource(R.string.humidity, weather.humidityPercentage),
        Icons.Default.WaterDrop
    )
    DetailRow(
        stringResource(R.string.wind),
        stringResource(R.string.wind_speed, weather.windSpeedText),
        Icons.Default.Air
    )
    DetailRow(
        stringResource(R.string.temperature), stringResource(
            R.string.min_max_temp,
            weather.minTemperature.roundToInt(),
            weather.maxTemperature.roundToInt()
        ), Icons.Default.Thermostat
    )
}