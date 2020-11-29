package com.example.weather.service

import com.example.weather.api.CurrentWeatherDto
import com.example.weather.api.WeatherHistoryDto
import com.example.weather.client.WeatherApiClient
import com.example.weather.entity.Weather
import com.example.weather.repository.WeatherRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.lang.Float.parseFloat

@Service
class WeatherService(private val weatherApiClient: WeatherApiClient,
                     private val repository: WeatherRepository
) {

    @Value("\${db.clear.probability.percentage}")
    lateinit var clearProbabilityPercentage: Number

    fun getCurrentWeather(city: String): CurrentWeatherDto {
        val currentWeather = weatherApiClient.getCurrentWeather(city)

        saveWeatherHistory(currentWeather, city)

        return currentWeather
    }

    fun getWeatherHistory(city: String): WeatherHistoryDto {
        val lastRecords = findNewestRecordsForCity(city)
        return transformToHistory(lastRecords)
    }

    private fun transformToHistory(lastRecords: List<Weather>): WeatherHistoryDto {
        val history = lastRecords.map { WeatherHistoryDto.WeatherDto(it.temp, it.pressure, it.umbrella) }
                .toTypedArray()

        val avgTemp = history.map { parseFloat(it.temp) }.sum() / history.size
        val avgPressure = history.map { parseFloat(it.pressure) }.sum() / history.size

        return WeatherHistoryDto(avgTemp, avgPressure, history)
    }

    private fun saveWeatherHistory(currentWeather: CurrentWeatherDto, city: String) {
        val record = Weather(temp = currentWeather.temp,
                city = city,
                pressure = currentWeather.pressure,
                umbrella = currentWeather.umbrella,
                timestamp = System.currentTimeMillis()
        )
        repository.save(record)

        //TODO execute on async thread
        clearWeatherHistoryOccasionally(city)
    }

    private fun clearWeatherHistoryOccasionally(city: String) {
        if (DBHelper.toggleOccasionally(clearProbabilityPercentage.toInt())) {
            clearOldRecords(city, findNewestRecordsForCity(city))
        }
    }

    private fun clearOldRecords(city: String, newestRecords: List<Weather>) {
        val oldRecords = repository.findByCityAndTimestampLessThan(city,
                System.currentTimeMillis() - OLD_MILLIS)
                .filter { !newestRecords.contains(it) }
        repository.deleteAll(oldRecords.toMutableList())
    }

    private fun findNewestRecordsForCity(city: String): List<Weather> {
        return repository.findByCityOrderByTimestampDesc(city, PageRequest.of(0, CITY_MAX_RECORDS))
    }

    companion object {
        private const val CITY_MAX_RECORDS: Int = 5
        private const val OLD_MILLIS: Int = 2000
    }
}