package com.custom.data.repository

import com.custom.core.model.CityModel
import com.custom.core.repository.CitySearchRepository
import com.custom.data.mapper.toDomainModel
import com.custom.data.remote.GeoApi
import javax.inject.Inject


class CitySearchRepositoryImpl @Inject constructor(private val api: GeoApi) :
    CitySearchRepository {

    override suspend fun searchCities(query: String): Result<List<CityModel>> {
        return try {

            val dtos = api.searchCities(query)
            val models = dtos.map { it.toDomainModel() }
            Result.success(models)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}