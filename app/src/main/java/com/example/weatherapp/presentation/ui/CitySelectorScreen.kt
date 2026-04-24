package com.example.weatherapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel

@Composable
fun CitySelectorScreen(viewModel: WeatherViewModel, onCityAdded: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = viewModel.searchQuery.value,
            onValueChange = { viewModel.searchQuery.value = it },
            label = { Text(stringResource(R.string.enter_city_name)) },
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        Button(
            onClick = {
                if (viewModel.searchQuery.value.isNotBlank()) {
                    viewModel.addCityQuery(viewModel.searchQuery.value)
                    onCityAdded()
                }
            },
            enabled = !viewModel.isLoadingCity.value
        ) {
            Text(stringResource(R.string.add_city))
        }
    }

    viewModel.errorMessage.value?.let { error ->
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}