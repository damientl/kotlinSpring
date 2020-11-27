package com.example.weather.context

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.MongoDBContainer

@Configuration
class MongoDBInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        CONTAINER.start()
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                "spring.data.mongodb.uri=${CONTAINER.replicaSetUrl}"
        )
    }
    companion object {
        val CONTAINER = MongoDBContainer("mongo:3.2.4")
    }
}