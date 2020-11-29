package com.example.weather.service

import org.junit.jupiter.api.Test

class DBHelperTest {

    @Test
    fun shouldToogleOccasionallyRare() {
        var total = 0
        for (i in 1..100) {
            val toggle = DBHelper.toggleOccasionally(1)
            if(toggle){
                total++
            }
        }
        println("Total rare: $total")
    }

    @Test
    fun shouldToogleOccasionallyOften() {
        var total = 0
        for (i in 1..100) {
            val toggle = DBHelper.toggleOccasionally(99)
            if(toggle){
                total++
            }
        }
        println("Total often: $total")
    }
}