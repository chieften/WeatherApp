package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiNinjasApi {
    @GET("v1/geocoding")
    suspend fun getCoordinates(
        @Query("city") city: String,
        @Query("country") country: String? = null,
        @Header("X-Api-Key") apiKey: String
    ): List<GeocodingResponse>
}