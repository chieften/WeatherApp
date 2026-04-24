package com.example.weatherapp.data.model

import com.squareup.moshi.Json

data class GeocodingResponse (
    @Json(name = "name") val name: String,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "country") val country: String
)