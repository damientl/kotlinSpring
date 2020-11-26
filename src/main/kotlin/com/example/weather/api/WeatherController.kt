package com.example.weather.api

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WeatherController {

    @GetMapping(path = ["/current"], consumes = [MediaType.ALL_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCurrentWeather(): String {
        return "a"
    }
}