package com.custom.domain.repository

import com.custom.domain.model.CityModel

interface CitySearchRepository {

    /**
     * Searches for cities that match the query.
     */
    suspend fun searchCities(query: String): Result<List<CityModel>>
}