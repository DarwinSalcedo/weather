package com.custom.data.remote

import com.custom.data.dto.CityDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApi {
    @GET("geo/1.0/direct")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
    ) : List<CityDto>
}