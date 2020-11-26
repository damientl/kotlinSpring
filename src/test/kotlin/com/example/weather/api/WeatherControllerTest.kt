package com.example.weather.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeatherControllerTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun shouldReturnCurrentWeather() {
        val entity = restTemplate.getForEntity("/current", String::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("a")

    }
}
