package com.custom.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.custom.home.R
import com.custom.home.domain.model.WeatherUiModel


@Composable
fun HeaderContent(
    onSearchClicked: () -> Unit,
    weather: WeatherUiModel
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
}