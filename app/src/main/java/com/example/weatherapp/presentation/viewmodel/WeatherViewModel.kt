package com.example.weatherapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.City
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.domain.WeatherUseCase
import kotlinx.coroutines.launch

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val data: WeatherResponse) : UiState()
    data class Error(val message: String) : UiState()
}

class WeatherViewModel(private val useCase: WeatherUseCase) : ViewModel() {
    val cities = mutableStateListOf<City>()
    val weatherStates = mutableStateMapOf<String, UiState>()
    val searchQuery = mutableStateOf("")
    val isLoadingCity = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun addCityQuery(cityName: String) {
        viewModelScope.launch {
            isLoadingCity.value = true
            errorMessage.value = null
            try {
                val city = useCase.findCity(cityName)
                if (city != null && !cities.any { it.name.equals(city.name, ignoreCase = true) }) {
                    cities.add(city)
                    fetchWeatherForCity(city)
                    searchQuery.value = ""
                } else {
                    errorMessage.value = "City not found or already added"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
            } finally {
                isLoadingCity.value = false
            }
        }
    }

    fun fetchWeatherForCity(city: City) {
        viewModelScope.launch {
            weatherStates[city.name] = UiState.Loading
            try {
                val weather = useCase.fetchWeather(city.latitude, city.longitude)
                weatherStates[city.name] = UiState.Success(weather)
            } catch (e: Exception) {
                weatherStates[city.name] = UiState.Error("Failed to load weather")
            }
        }
    }

    fun removeCity(city: City) {
        cities.remove(city)
        weatherStates.remove(city.name)
    }

    fun retryFetch(city: City) {
        fetchWeatherForCity(city)
    }
}