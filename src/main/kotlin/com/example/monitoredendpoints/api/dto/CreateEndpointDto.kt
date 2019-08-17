package com.example.monitoredendpoints.api.dto

import com.example.monitoredendpoints.model.enum.*
import javax.validation.constraints.*

class CreateEndpointDto(
        @NotBlank(message = "name must be filled")
        val name: String,

        @NotBlank(message = "url must be filled")
        val url: String,

        @NotNull(message = "operation must be filled")
        val operation: RestOperation,

        @NotNull(message = "monitoring interval must be filled")
        val monitoringInterval: Int,

        val owners: List<String> = listOf()
)