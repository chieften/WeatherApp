package com.example.weatherapp.domain

import com.example.weatherapp.data.model.City
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.repository.WeatherRepository

class WeatherUseCase(private val repository: WeatherRepository) {
    suspend fun findCity(cityName: String, country: String? = null): City? {
        return repository.getCityCoordinates(cityName, country)
    }

    suspend fun fetchWeather(latitude: Double, longitude: Double): WeatherResponse {
        return repository.getWeatherForecast(latitude, longitude)
    }
}