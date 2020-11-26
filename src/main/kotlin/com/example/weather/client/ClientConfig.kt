package com.example.weather.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ClientConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate();
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return JsonMapper.builder().addModule(KotlinModule()).build()
    }
}