package com.example.weather.api

import com.example.weather.client.WeatherApiClient
import com.example.weather.context.MongoDBInitializer
import com.example.weather.entity.Weather
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
import org.springframework.boot.test.web.client.getForEntity
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

        repository.deleteAll()
    }

    @Test
    fun getCurrentWeatherShouldReturnCurrentWeatherAndSaveRecord() {
        // given
        mockWebServer!!.enqueue(MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", "application/json")
                .setBody(WeatherApiResponseFixtures.CURRENT_WEATHER)
        )

        // when
        val response = restTemplate.getForEntity<String>("/current?location=Berlin");

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isNotNull

        val body = mapper.readValue<CurrentWeatherDto>(response.body!!)
        assertThat(body.temp).isEqualTo("280.32")
        assertThat(body.pressure).isEqualTo("1012")
        assertThat(body.umbrella).isEqualTo(true)

        val records = repository.findAll()
        assertThat(records.size).isEqualTo(1)
        val record = records[0]
        assertThat(record).isNotNull
        assertThat(record.city).isEqualTo("Berlin")
        assertThat(record.temp).isEqualTo("280.32")
    }


    @Test
    fun getCurrentWeatherShouldReturn500Error() {

        // given
        mockWebServer!!.enqueue(MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", "application/json")
                .setBody(WeatherApiResponseFixtures.CURRENT_WEATHER_INVALID_JSON)
        )

        // when
        val response = restTemplate.getForEntity<String>("/current?location=Berlin");

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(response.body).isNotNull
    }

    @Test
    fun getHistoryShouldReturnAvailableHistory(){

        // given
        for (i in 1..10) {
            val record = Weather(temp = "12",
                    city = "Berlin",
                    pressure = "10",
                    umbrella = true,
                    timestamp = System.currentTimeMillis()
            )
            repository.save(record)
        }

        // when
        val response = restTemplate.getForEntity<String>("/history?location=Berlin")

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isNotNull

        val body = mapper.readValue<WeatherHistoryDto>(response.body!!)

        assertThat(body.history).hasAtLeastOneElementOfType(WeatherHistoryDto.WeatherDto::class.java)
    }
}
