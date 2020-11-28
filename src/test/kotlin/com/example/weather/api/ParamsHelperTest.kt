package com.example.weather.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ParamsHelperTest {
    @Test
    fun shouldExtractCityName(){
        val result = ParamsHelper.extractCityName("Berlin")

        assertThat(result).isEqualTo("Berlin")
    }
    @Test
    fun shouldExtractCityNameWithComma(){
        val result = ParamsHelper.extractCityName("Berlin,de")

        assertThat(result).isEqualTo("Berlin")
    }
}