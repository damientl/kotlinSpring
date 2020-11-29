package com.example.weather.service

import com.example.weather.api.WeatherApiResponseFixtures
import com.example.weather.client.WeatherApiClient
import com.example.weather.context.MongoDBInitializer
import com.example.weather.repository.WeatherRepository
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(initializers = [MongoDBInitializer::class])
class WeatherServiceIntegrationTest(
        @Autowired val repository: WeatherRepository,
        @Autowired val service: WeatherService,
        @Autowired val client: WeatherApiClient) {

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

        repository.deleteAll()
    }

    @Test
    fun shouldSave5Records() {
        val city = "a"

        for (i in 1..5) {
            mockWebServer!!.enqueue(MockResponse().setResponseCode(HttpStatus.OK.value())
                    .setHeader("Content-Type", "application/json")
                    .setBody(WeatherApiResponseFixtures.CURRENT_WEATHER)
            )
            service.getCurrentWeather(city)
        }

        val records = repository.findAll()
        Assertions.assertThat(records.size).isEqualTo(5)
        val record = records[0]
        Assertions.assertThat(record).isNotNull
    }

    @Test
    fun shouldSaveAtLeast5Records() {
        val city = "a"

        for (i in 1..10) {
            mockWebServer!!.enqueue(MockResponse().setResponseCode(HttpStatus.OK.value())
                    .setHeader("Content-Type", "application/json")
                    .setBody(WeatherApiResponseFixtures.CURRENT_WEATHER)
            )
            service.getCurrentWeather(city)
        }

        val records = repository.findAll()
        Assertions.assertThat(records.size).isGreaterThanOrEqualTo(5)
        val record = records[0]
        Assertions.assertThat(record).isNotNull
    }
}