package com.example.monitoredendpoints.api.dto

import com.example.monitoredendpoints.model.*
import com.example.monitoredendpoints.model.enum.*
import java.time.*
import javax.validation.constraints.*

data class MonitoredEndpointDto(
        val id: String,

        @NotBlank(message = "name must be filled")
        val name: String,

        @NotBlank(message = "url must be filled")
        val url: String,

        @NotNull(message = "operation must be filled")
        val operation: RestOperation,

        val creation: LocalDateTime,

        val lastCheck: LocalDateTime,

        @NotNull(message = "monitoring interval must be filled")
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
                        operation = model.operation,
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