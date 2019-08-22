package com.example.monitoredendpoints.api.dto

import javax.validation.constraints.*

class CreateEndpointDto(
        @field:NotBlank(message = "name must be filled")
        val name: String,

        @field:NotBlank(message = "url must be filled")
        val url: String,

        @field:NotNull(message = "monitoring interval must be filled")
        val monitoringInterval: Int,

        val owners: List<String> = listOf()
)