package com.example.weather.service

import com.example.weather.api.CurrentWeatherDto
import com.example.weather.client.WeatherApiClient
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WeatherServiceTest {
    private val client = mockk<WeatherApiClient>()

    @Test
    fun getCurrentWeatherShouldReturnApiData() {
        // given
        val expectedResult = CurrentWeatherDto("1", "1", true)
        every { client.getCurrentWeather() } returns expectedResult

        // when
        val result = WeatherService(client).getCurrentWeather()

        //then
        assertThat(result.temp).isEqualTo(expectedResult.temp)
    }
}