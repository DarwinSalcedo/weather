package com.custom.home.ui

import com.custom.home.domain.model.CityUiModel

sealed class SearchState {
    data object Init : SearchState()

    data object Searching : SearchState()

    data class Results(val cities: List<CityUiModel>) : SearchState()

    data class Error(val message: String) : SearchState()
}