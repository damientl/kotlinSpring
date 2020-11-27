package com.example.weather.service

import com.example.weather.api.CurrentWeatherDto
import com.example.weather.client.WeatherApiClient
import com.example.weather.repository.WeatherRepository
import org.springframework.stereotype.Service

@Service
class WeatherService (private val weatherApiClient: WeatherApiClient, private val repository: WeatherRepository){
    fun getCurrentWeather(): CurrentWeatherDto {
        val currentWeather = weatherApiClient.getCurrentWeather()

        saveWeatherHistory(currentWeather);

        return currentWeather
    }

    private fun saveWeatherHistory(currentWeather: CurrentWeatherDto) {
        repository.findAll()

        clearWeatherHistory()
    }

    private fun clearWeatherHistory() {
    }
}