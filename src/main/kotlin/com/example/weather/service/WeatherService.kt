package com.example.weather.service

import com.example.weather.api.CurrentWeatherDto
import com.example.weather.client.WeatherApiClient
import com.example.weather.entity.Weather
import com.example.weather.repository.WeatherRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class WeatherService (private val weatherApiClient: WeatherApiClient,
                      private val repository: WeatherRepository
){

    @Value("\${db.clear.probability.percentage}")
    lateinit var clearProbabilityPercentage: Number

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
        if(DBHelper.toggleOccasionally(clearProbabilityPercentage.toInt())){
            clearOldRecords(city, findNewestRecordsForCity(city))
        }
    }

    private fun clearOldRecords(city: String, newestRecords: List<Weather>) {
        val oldRecords = repository.findByCityAndOldTimestamp(city,
                System.currentTimeMillis() - OLD_MILIS)
                .filter { !newestRecords.contains(it) }
        repository.deleteAll(oldRecords.toMutableList())
    }

    private fun findNewestRecordsForCity(city: String): List<Weather> {
        return repository.findByCityOrderByTimestampDesc(city, PageRequest.of(0,CITY_MAX_RECORDS))
    }

    companion object {
        private const val CITY_MAX_RECORDS: Int = 5
        private const val OLD_MILIS: Int = 2000
    }
}