package com.example.weather.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Weather (@Id val id: ObjectId = ObjectId.get(), val temp: String, val city: String,
            val timestamp: Long){
}