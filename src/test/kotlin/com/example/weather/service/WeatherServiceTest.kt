package com.example.weather.service

import com.example.weather.api.CurrentWeatherDto
import com.example.weather.client.WeatherApiClient
import com.example.weather.entity.Weather
import com.example.weather.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WeatherServiceTest {
    private val client = mockk<WeatherApiClient>()
    private val repository = mockk<WeatherRepository>()

    @Test
    fun getCurrentWeatherShouldReturnApiData() {
        // given
        val city = "a"
        val expectedResult = CurrentWeatherDto("1", "1", true)
        every { client.getCurrentWeather(city) } returns expectedResult
        every { repository.save(any())} returns Weather(temp = "1", city = "a", timestamp = 1,
            pressure = "123", umbrella = false
        )

        val service = WeatherService(client, repository)
        service.clearProbabilityPercentage = 10
        // when
        val result = service.getCurrentWeather(city)

        //then
        assertThat(result.temp).isEqualTo(expectedResult.temp)
    }
}