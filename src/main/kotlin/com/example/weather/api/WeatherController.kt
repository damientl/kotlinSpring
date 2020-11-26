package com.example.weather.api

import com.example.weather.service.WeatherService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WeatherController(private val weatherService: WeatherService) {

    @GetMapping(path = ["/current"], consumes = [MediaType.ALL_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCurrentWeather(): CurrentWeatherDto {
        return weatherService.getCurrentWeather()
    }
}