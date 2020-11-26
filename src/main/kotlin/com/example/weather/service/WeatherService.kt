package com.example.weather.service

import com.example.weather.api.CurrentWeatherDto
import com.example.weather.client.WeatherApiClient
import org.springframework.stereotype.Service

@Service
class WeatherService (private val weatherApiClient: WeatherApiClient){
    fun getCurrentWeather(): CurrentWeatherDto {
        val currentWeather = weatherApiClient.getCurrentWeather()

        saveWeatherHistory(currentWeather);

        return currentWeather
    }

    private fun saveWeatherHistory(currentWeather: CurrentWeatherDto) {

        clearWeatherHistory()
    }

    private fun clearWeatherHistory() {
    }
}