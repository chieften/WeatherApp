package com.example.weatherapp.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.presentation.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherCard(
    cityName: String,
    uiState: UiState,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${stringResource(R.string.forecast)} $cityName",
                style = MaterialTheme.typography.titleMedium
            )
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                    Text(stringResource(R.string.loading))
                }
                is UiState.Success -> {
                    val temp = uiState.data.hourly.temperature.firstOrNull()
                    Text(
                        text = "${stringResource(R.string.temperature)}: ${temp?.toInt()}°C",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                is UiState.Error -> {
                    Text(uiState.message, color = MaterialTheme.colorScheme.error)
                    TextButton(onClick = onRetry) {
                        Text(stringResource(R.string.retry))
                    }
                }
                else -> {}
            }
        }
    }
}