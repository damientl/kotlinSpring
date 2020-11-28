package com.example.weather.service

import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import kotlin.random.Random

@Service
class DBHelper {

    companion object {
        /**
         * @param clearProbabilityPercentage must be between 1 - 99.
         */
        fun toggleOccasionally(clearProbabilityPercentage: Int) : Boolean {
            if(clearProbabilityPercentage < 1 || clearProbabilityPercentage > 99) {
                throw IllegalArgumentException("clearProbabilityPercentage must be between 1 - 99")
            }

            return Random.nextInt(0, 101 - clearProbabilityPercentage) == 0
        }
    }
}