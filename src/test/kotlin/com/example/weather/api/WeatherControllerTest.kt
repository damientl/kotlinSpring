package com.example.weather.api

import com.example.weather.client.WeatherApiClient
import com.example.weather.context.MongoDBInitializer
import com.example.weather.repository.WeatherRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [MongoDBInitializer::class])
class WeatherControllerTest(@Autowired val restTemplate: TestRestTemplate,
                            @Autowired val client: WeatherApiClient,
                            @Autowired val repository: WeatherRepository,
                            @Autowired val mapper: ObjectMapper
) {

    private var mockWebServer: MockWebServer? = null

    @BeforeEach
    fun beforeAll() {
        mockWebServer = MockWebServer()
        mockWebServer!!.start(client.weatherApiPort.toInt())
    }

    @AfterEach
    fun afterAll() {
        mockWebServer!!.shutdown()
        mockWebServer = null
    }

    @Test
    fun shouldReturnCurrentWeather() {
        // given
        mockWebServer!!.enqueue(MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", "application/json")
                .setBody(WeatherApiResponseFixtures.CURRENT_WEATHER)
        )

        repository.findAll()

        // when
        val response = restTemplate.getForEntity("/current?location=Berlin",
                String::class.java);


        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isNotNull

        val body = mapper.readValue<CurrentWeatherDto>(response.body!!)
        assertThat(body.temp).isEqualTo("280.32")
        assertThat(body.pressure).isEqualTo("1012")
        assertThat(body.umbrella).isEqualTo(true)

    }
}
