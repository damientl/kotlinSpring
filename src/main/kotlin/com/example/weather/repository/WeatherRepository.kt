package com.example.weather.repository

import com.example.weather.entity.Weather
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface WeatherRepository : MongoRepository<Weather, String> {
    fun findByCityOrderByTimestampDesc(city: String, pageable: Pageable): List<Weather>

    fun findByCityAndTimestampLessThan(city: String, oldTimestamp: Long): List<Weather>
}