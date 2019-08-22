package com.example.monitoredendpoints.config

import org.springframework.beans.factory.annotation.*
import org.springframework.stereotype.*

@Component
class Properties {
    @Value("\${custom.maxResultPersistence}")
    var maxMonitoringPersistence: Int = 10
}