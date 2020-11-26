package com.example.weather.client

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherApiResponse(val weather: Array<WeatherDto>, val main: MainDto) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherDto(val main: String)
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class MainDto(val temp: String, val pressure: String)
}
