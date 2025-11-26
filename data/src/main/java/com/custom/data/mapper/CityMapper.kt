package com.custom.data.mapper

import com.custom.data.dto.CityDto
import com.custom.domain.model.CityModel

fun CityDto.toDomainModel(): CityModel {
    return CityModel(
        name = this.name,
        country = this.country,
        latitude = this.latitude,
        longitude = this.longitude
    )
}