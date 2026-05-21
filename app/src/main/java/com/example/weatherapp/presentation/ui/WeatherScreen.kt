package com.example.weatherapp.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weatherapp.presentation.ui.components.CityItem
import com.example.weatherapp.presentation.ui.components.WeatherCard
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    LazyColumn(modifier = Modifier) {
        items(viewModel.cities) { city ->
            CityItem(
                city = city,
                onRemove = { viewModel.removeCity(city) },
                onWeatherClick = { viewModel.fetchWeatherForCity(city) }
            )
            viewModel.weatherStates[city.name]?.let { state ->
                WeatherCard(
                    cityName = city.name,
                    uiState = state,
                    onRetry = { viewModel.retryFetch(city) }
                )
            }
        }
    }
}