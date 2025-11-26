package com.custom.data.mapper

import android.location.Location
import com.custom.domain.model.LocationModel


fun Location.toDomainModel(): LocationModel {
    return LocationModel(
        latitude = this.latitude,
        longitude = this.longitude
    )
}