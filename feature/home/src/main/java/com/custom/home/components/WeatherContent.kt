package com.custom.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.custom.common.components.DefaultBodyContent
import com.custom.home.R
import com.custom.home.domain.model.WeatherUiModel

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
        HeaderContent(onSearchClicked, weather)

        Spacer(Modifier.height(32.dp))

        if (!weather.isPlaceholder) {
            MainContent(weather)
        } else {
            DefaultBodyContent(
                Icons.Default.TravelExplore,
                null,
                stringResource(R.string.welcome),
                stringResource(R.string.search_a_city_to_get_started)
            )
        }
    }
}
