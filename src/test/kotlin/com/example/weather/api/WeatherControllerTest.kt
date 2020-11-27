package com.example.weather.api

import com.example.weather.client.WeatherApiClient
import com.example.weather.context.MongoDBInitializer
import com.example.weather.repository.WeatherRepository
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [MongoDBInitializer::class])
class WeatherControllerTest(@Autowired val restTemplate: TestRestTemplate,
                            @Autowired val client: WeatherApiClient,
                            @Autowired val repository: WeatherRepository
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
        val entity = restTemplate.getForEntity("/current", CurrentWeatherDto::class.java)

        // then
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isNotNull
        val body = entity.body!!
        assertThat(body.temp).isEqualTo("280.32")
        assertThat(body.pressure).isEqualTo("1012")
        assertThat(body.umbrella).isEqualTo(true)

    }
}
