package com.example.monitoredendpoints.config

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.*
import org.springframework.boot.web.client.*
import org.springframework.context.annotation.*
import org.springframework.web.client.*


@Configuration
class Beans {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate = builder.build()

    @Bean
    fun objectMapper(): ObjectMapper =
            ObjectMapper()
                    .registerModule(KotlinModule())
}