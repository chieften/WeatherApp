package com.example.weatherapp.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherapp.R
import com.example.weatherapp.di.NetworkModule
import com.example.weatherapp.presentation.ui.CitySelectorScreen
import com.example.weatherapp.presentation.ui.CityWeatherCard
import com.example.weatherapp.presentation.ui.DetailWeatherScreen
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: WeatherViewModel = viewModel {
        WeatherViewModel(NetworkModule.useCase)
    }

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            WeatherAppContent(
                viewModel = viewModel,
                onCityClick = { cityName ->
                    navController.navigate("detail/$cityName")
                }
            )
        }
        composable(
            route = "detail/{cityName}",
            arguments = listOf(navArgument("cityName") { type = NavType.StringType })
        ) { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            DetailWeatherScreen(
                cityName = cityName,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppContent(
    viewModel: WeatherViewModel = viewModel {
        WeatherViewModel(NetworkModule.useCase)
    },
    onCityClick: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CitySelectorScreen(viewModel = viewModel, onCityAdded = {})

            if (viewModel.cities.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.no_cities))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(viewModel.cities) { city ->
                        CityWeatherCard(
                            city = city,
                            uiState = viewModel.weatherStates[city.name],
                            onRemove = { viewModel.removeCity(city) },
                            onRetry = { viewModel.retryFetch(city) },
                            onClick = { onCityClick(city.name) }
                        )
                    }
                }
            }
        }
    }
}