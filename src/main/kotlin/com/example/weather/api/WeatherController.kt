package com.example.weather.api

import com.example.weather.service.WeatherService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class WeatherController(private val weatherService: WeatherService) {

    @GetMapping(path = ["/current"], consumes = [MediaType.ALL_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCurrentWeather(@RequestParam location: String): CurrentWeatherDto {
        validateLocationFormat(location)

        //TODO currently not considering country.
        // Cities with same name on different countries will be considered equal.
        val city = ParamsHelper.extractCityName(location)


        return weatherService.getCurrentWeather(city)
    }

    @GetMapping(path = ["/history"], consumes = [MediaType.ALL_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getHistory(@RequestParam location: String) : WeatherHistoryDto {
        val city = ParamsHelper.extractCityName(location)
        return weatherService.getWeatherHistory(city)
    }

    private fun validateLocationFormat(location: String) {
        if(location.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Name of the city is blank")
        }

    }
}