package com.example.weather.repository

import com.example.weather.entity.Weather
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface WeatherRepository : MongoRepository<Weather, String> {
    fun findByCityOrderByTimestampDesc(city: String, pageable: Pageable): List<Weather>

    @Query("{'city' : ?0, timestamp : {\$lt : ?1}}")
    fun findByCityAndOldTimestamp(city: String, oldTimestamp: Long): List<Weather>
}