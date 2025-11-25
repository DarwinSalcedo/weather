package com.custom.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.custom.home.R
import com.custom.home.domain.model.WeatherUiModel
import kotlin.math.roundToInt

@Composable
fun WeatherContent(
    weather: WeatherUiModel,
    onSearchClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSearchClicked() }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_a_city)
                )
                Spacer(Modifier.width(8.dp))

                val searchPrompt = if (weather.isPlaceholder) {
                    stringResource(R.string.search_a_city)
                } else {
                    stringResource(R.string.search_other_city)
                }
                Text(searchPrompt, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.height(32.dp))

        if (!weather.isPlaceholder) {

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
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    Icons.Default.TravelExplore,
                    contentDescription = null,
                    modifier = Modifier.size(96.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(stringResource(R.string.welcome), style = MaterialTheme.typography.headlineMedium)
                Text(
                    stringResource(R.string.search_a_city_to_get_started),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
