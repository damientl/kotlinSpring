package com.example.weather.repository

import com.example.weather.entity.Weather
import org.springframework.data.mongodb.repository.MongoRepository

interface WeatherRepository : MongoRepository<Weather, String> {
}