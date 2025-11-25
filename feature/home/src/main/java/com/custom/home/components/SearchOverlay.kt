package com.custom.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.custom.home.R
import com.custom.home.domain.model.CityUiModel
import com.custom.home.ui.MINIMUM_CHARACTERS_TO_SEARCH
import com.custom.home.ui.SearchState


@Composable
fun SearchOverlay(
    searchState: SearchState,
    onQueryChanged: (String) -> Unit,
    onCitySelected: (CityUiModel) -> Unit,
    onClose: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val isSearching = searchState !is SearchState.Init

    AnimatedVisibility(
        visible = isSearching,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.close_search))
                    }

                    OutlinedTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            onQueryChanged(it)
                        },
                        label = { Text(stringResource(R.string.name_of_the_city)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    AnimatedVisibility(visible = query.isNotEmpty()) {
                        IconButton(onClick = {
                            query = ""
                            onQueryChanged("")
                        }) {
                            Icon(Icons.Default.Close, contentDescription = stringResource(R.string.clear_query))
                        }
                    }
                }

                when (val state = searchState) {
                    is SearchState.Searching -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                            CircularProgressIndicator(Modifier.padding(top = 16.dp))
                        }
                    }

                    is SearchState.Results -> {
                        if (state.cities.isEmpty() && query.length >= MINIMUM_CHARACTERS_TO_SEARCH) {
                            Text(
                                stringResource(R.string.not_results_for, query),
                                modifier = Modifier.padding(16.dp)
                            )
                        } else if (state.cities.isEmpty()) {
                            Text(
                                stringResource(
                                    R.string.write_at_least_characters_to_search,
                                    MINIMUM_CHARACTERS_TO_SEARCH
                                ),
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            LazyColumn(Modifier.fillMaxSize()) {
                                items(state.cities) { city ->
                                    ListItem(
                                        headlineContent = { Text(city.name + " (" + city.country + ")") },
                                        supportingContent = {
                                            Text(
                                                stringResource(
                                                    R.string.coordinates_lat_lon,
                                                    city.latitude,
                                                    city.longitude
                                                ),
                                            )
                                        },
                                        leadingContent = {
                                            Icon(
                                                Icons.Default.LocationCity,
                                                contentDescription = null
                                            )
                                        },
                                        modifier = Modifier.clickable {
                                            query = ""
                                            onQueryChanged("")
                                            onCitySelected(city)
                                        }
                                    )
                                    HorizontalDivider()
                                }
                            }
                        }
                    }

                    is SearchState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    SearchState.Init -> Unit
                }
            }
        }
    }
}