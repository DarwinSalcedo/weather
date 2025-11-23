package com.custom.data.mapper

import com.custom.core.model.CityModel
import com.custom.data.dto.CityDto

fun CityDto.toDomainModel(): CityModel {
    return CityModel(
        name = this.name,
        country = this.country,
        latitude = this.latitude,
        longitude = this.longitude
    )
}