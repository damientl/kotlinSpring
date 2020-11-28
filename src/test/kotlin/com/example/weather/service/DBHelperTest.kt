package com.example.weather.service

import org.junit.jupiter.api.Test

class DBHelperTest {

    @Test
    fun shouldToogleOccasionallyRare() {
        for (i in 1..55) {
            val toggle = DBHelper.toggleOccasionally(1)
            println(toggle)
        }
    }

    @Test
    fun shouldToogleOccasionallyOften() {
        for (i in 1..55) {
            val toggle = DBHelper.toggleOccasionally(99)
            println(toggle)
        }
    }
}