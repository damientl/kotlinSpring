package com.example.weather.client

import com.example.weather.api.CurrentWeatherDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class WeatherApiClient(val restTemplate: RestTemplate, val objectMapper: ObjectMapper) {

    @Value("\${weather-api.port}")
    lateinit var weatherApiPort: Number

    @Value("\${weather-api.uri}")
    lateinit var weatherApiUri: String


    fun getCurrentWeather(): CurrentWeatherDto {

        val response = restTemplate.getForEntity(
                weatherApiUri + ":" + weatherApiPort + "/data/2.5/weather?" + "q=London&appid=123",
                String::class.java)

        return transform(mapBody(response.body))
    }

    private fun mapBody(body: String?): WeatherApiResponse? {
        return objectMapper.readValue(body, WeatherApiResponse::class.java)
    }

    private fun transform(body: WeatherApiResponse?): CurrentWeatherDto {
        if (body == null) {
            throw RuntimeException("Weather api null response")
        }
        // TODO array
        return CurrentWeatherDto(body.main.temp, body.main.pressure, transformWeather(body.weather[0].main))
    }

    // TODO
    private fun transformWeather(main: String): Boolean {
        return true
    }
}