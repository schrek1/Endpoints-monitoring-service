package com.example.monitoredendpoints.api.dto

import com.example.monitoredendpoints.model.*
import java.time.*

data class MonitoringResultDto(
        val id: String,

        val checkTime: LocalDateTime,

        val statusCode: Int,

        val responseBody: String

) {
    companion object {
        fun fromModel(model: MonitoringResult) =
                MonitoringResultDto(
                        id = model.id.toString(),
                        checkTime = model.checkTime,
                        statusCode = model.statusCode,
                        responseBody = model.responseBody
                )
    }
}