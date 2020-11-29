package com.example.weather.api

data class WeatherHistoryDto(val avgTemp: Float, val avgPressure: Float, val history: Array<WeatherDto>) {
    data class WeatherDto(val temp: String, val pressure: String, val umbrella: Boolean)
}
