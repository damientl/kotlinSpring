package com.example.weather.service

import com.example.weather.api.CurrentWeatherDto
import com.example.weather.client.WeatherApiClient
import com.example.weather.entity.Weather
import com.example.weather.repository.WeatherRepository
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class WeatherService (private val weatherApiClient: WeatherApiClient, private val repository: WeatherRepository){

    fun getCurrentWeather(city: String): CurrentWeatherDto {
        val currentWeather = weatherApiClient.getCurrentWeather(city)

        saveWeatherHistory(currentWeather, city);

        return currentWeather
    }

    private fun saveWeatherHistory(currentWeather: CurrentWeatherDto, city: String) {
        val record = Weather(temp = currentWeather.temp,
                city = city,
                timestamp = System.currentTimeMillis()
        )
        repository.save(record)

        clearWeatherHistoryOccasionally(city)
    }

    private fun clearWeatherHistoryOccasionally(city: String) {
        if(Random.nextInt(0, 100 - OCCASIONAL_PROBABILITY_PERCENTAGE) == 0){
            val records = findLastRecordsForCity(city)
            clearOldRecords(city, records)
        }
    }

    private fun clearOldRecords(city: String, newestRecords: Array<Weather>) {
        val oldRecords = repository.findByCityAndIdNotInAndOldTimestamp(city, newestRecords.map { it.id }.toTypedArray(),
                System.currentTimeMillis() - OLD_MILIS)
        repository.deleteAll(oldRecords.toMutableList())
    }

    private fun findLastRecordsForCity(city: String): Array<Weather> {
        return repository.findByCityOrderByTimestampDescLimitedTo(city, CITY_MAX_RECORDS)
    }

    companion object {
        private const val CITY_MAX_RECORDS: Int = 5
        private const val OLD_MILIS: Int = 2000
        private const val OCCASIONAL_PROBABILITY_PERCENTAGE = 1
    }
}