package com.custom.core.repository

import com.custom.core.model.CityModel

interface CitySearchRepository {

    /**
     * Searches for cities that match the query.
     */
    suspend fun searchCities(query: String): Result<List<CityModel>>
}