package com.custom.core.repository

import com.custom.core.model.LocationModel
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getLocationUpdates(): Flow<LocationModel>
}