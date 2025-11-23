package com.custom.data.dto

import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
)