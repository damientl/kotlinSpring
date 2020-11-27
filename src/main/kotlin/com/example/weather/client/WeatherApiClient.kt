package com.example.weather.client

import com.example.weather.api.CurrentWeatherDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Service
class WeatherApiClient(val restTemplate: RestTemplate, val objectMapper: ObjectMapper) {

    @Value("\${weather-api.port}")
    lateinit var weatherApiPort: Number

    @Value("\${weather-api.uri}")
    lateinit var weatherApiUri: String


    fun getCurrentWeather(city: String): CurrentWeatherDto {

        val response = restTemplate.getForEntity<String>(
                weatherApiUri + ":" + weatherApiPort + "/data/2.5/weather?" + "q=London&appid=123")

        return transform(mapBody(response.body))
    }

    private fun mapBody(body: String?): WeatherApiResponse {
        if (body == null) {
            throw RuntimeException("Weather api null response")
        }
        return objectMapper.readValue(body)
    }

    private fun transform(body: WeatherApiResponse): CurrentWeatherDto {
        // TODO array
        return CurrentWeatherDto(body.main.temp, body.main.pressure, transformWeather(body.weather[0].main))
    }

    // TODO
    private fun transformWeather(main: String): Boolean {
        return true
    }
}