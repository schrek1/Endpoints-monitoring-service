package com.example.monitoredendpoints.api.dto

import com.example.monitoredendpoints.model.*
import java.time.*
import javax.validation.constraints.*

data class MonitoredEndpointDto(
        val id: String,

        val name: String,

        val url: String,

        val creation: LocalDateTime,

        val lastCheck: LocalDateTime,

        val monitoringInterval: Int,

        val owners: List<String>,

        val monitoringResult: List<MonitoringResultDto>
) {
    companion object {
        fun fromModel(model: MonitoredEndpoint) =
                MonitoredEndpointDto(
                        id = model.id.toString(),
                        name = model.name,
                        url = model.url,
                        creation = model.creation,
                        lastCheck = model.lastCheck,
                        monitoringInterval = model.monitoringInterval,
                        owners = model.owners.asSequence()
                                .map { it.id.toString() }
                                .toList(),
                        monitoringResult = model.monitoringResults.asSequence()
                                .map { MonitoringResultDto.fromModel(it) }
                                .toList()
                )
    }
}