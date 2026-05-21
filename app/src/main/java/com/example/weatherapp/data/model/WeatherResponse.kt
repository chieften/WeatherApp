package com.example.weatherapp.data.model

import com.squareup.moshi.Json

data class WeatherResponse(
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "timezone") val timezone: String,
    @Json(name = "hourly") val hourly: Hourly
) {
    data class Hourly(
        @Json(name = "time") val time: List<String>,
        @Json(name = "temperature_2m") val temperature: List<Double>
    )
}