package com.example.weather.api

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class ParamsHelper {
    companion object {
        fun extractCityName(location: String): String {
            if(location.isEmpty()) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Name of the city does not conform to pattern: e.g. Berlin or Berlin,de")
            }
            val cityWithComma = Regex("(.+),.*").find(location)

            if (cityWithComma != null) {
                val (name) = cityWithComma.destructured
                return name
            }
            return location
        }
    }
}