package com.example.monitoredendpoints

import org.springframework.boot.*
import org.springframework.boot.autoconfigure.*
import org.springframework.scheduling.annotation.*

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
