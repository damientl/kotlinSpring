package com.example.weather.repository

import com.example.weather.entity.Weather
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface WeatherRepository : MongoRepository<Weather, String> {
    @Query("select w from Weather where w.city = ?1 ORDER BY w.timestamp DESC LIMIT ?2")
    fun findByCityOrderByTimestampDescLimitedTo(city: String, limit: Int): Array<Weather>

    @Query("select w from Weather where w.city = ?1 and w.id NOT IN (?2) and w.timestamp < ?3")
    fun findByCityAndIdNotInAndOldTimestamp(city: String, ids: Array<ObjectId>, oldTimestamp: Long): Array<Weather>
}