package com.example.weather.client

import com.example.weather.api.CurrentWeatherDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

    @Value("\${weather-api.appId}")
    lateinit var appId: String

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun getCurrentWeather(city: String): CurrentWeatherDto {
        try {
            log.debug("Request Weather API for city {}", city)
            val response = restTemplate.getForEntity<String>(
                    "$weatherApiUri:$weatherApiPort/data/2.5/weather?q=$city&appid=$appId")
            return transform(mapBody(response.body))
        } catch (e: MissingKotlinParameterException) {
            log.error("Weather API invalid response", e)
            throw RuntimeException("Weather API invalid response")
        }
    }

    private fun mapBody(body: String?): WeatherApiResponse {
        if (body == null) {
            throw RuntimeException("Weather api null response")
        }
        return objectMapper.readValue(body)
    }

    private fun transform(body: WeatherApiResponse): CurrentWeatherDto {
        return CurrentWeatherDto(body.main.temp, body.main.pressure, transformWeather(body.weather))
    }

    private fun transformWeather(weather: Array<WeatherApiResponse.WeatherDto>): Boolean {
        if(weather.isEmpty()) {
            log.error("Weather API did not return weather information. weather array is empty. Response: {}", weather)
            throw RuntimeException("Weather API did not return weather information. weather array is empty")
        }

        return weather.map { transformWeatherIn(it.main) }.filter { it }.isNotEmpty()
    }

    private fun transformWeatherIn(main: String): Boolean {
        return when (main) {
            "Thunderstorm", "Drizzle", "Rain" -> true
            else -> false
        }
    }
}