package com.custom.domain.repository

import com.custom.domain.model.LocationModel
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getLocationUpdates(): Flow<LocationModel>
}