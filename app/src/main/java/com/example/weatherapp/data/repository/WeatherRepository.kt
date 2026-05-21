package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.City
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.remote.ApiNinjasApi
import com.example.weatherapp.data.remote.OpenMeteoApi

class WeatherRepository (
    private val openMeteoApi: OpenMeteoApi,
    private val apiNinjasApi: ApiNinjasApi,
    private val apiKey: String
) {
    suspend fun getCityCoordinates(cityName: String, country: String? = null): City? {
        return try {
            val response = apiNinjasApi.getCoordinates(cityName, country, apiKey)
            response.firstOrNull()?.let {
                City(it.name, it.latitude, it.longitude, it.country)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getWeatherForecast(latitude: Double, longitude: Double):    WeatherResponse {
        return openMeteoApi.getWeather(latitude, longitude)
    }
}