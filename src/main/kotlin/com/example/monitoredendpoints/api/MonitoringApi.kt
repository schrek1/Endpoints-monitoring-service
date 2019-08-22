package com.example.monitoredendpoints.api

import com.example.monitoredendpoints.api.dto.*
import com.example.monitoredendpoints.response.*
import com.example.monitoredendpoints.service.*
import com.example.monitoredendpoints.utils.*
import org.springframework.http.*
import org.springframework.security.core.*
import org.springframework.validation.annotation.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import javax.validation.*

@RestController
@RequestMapping("/api/v1/monitoring")
@Validated
class MonitoringApi(
        private val endpointService: EndpointService
) {

    @GetMapping
    fun getEndpointsForUser(authentication: Authentication) = endpointService.getEndpointsForUser(authentication.getToken()).asSequence()
            .map { MonitoredEndpointDto.fromModel(it) }
            .toList()

    // todo remove
    @GetMapping("/all")
    fun getAllEndpoints() = endpointService.getAllEndpoints().asSequence()
            .map { MonitoredEndpointDto.fromModel(it) }
            .toList()

    @PostMapping
    fun createEndpoint(@Valid @RequestBody endpointDto: CreateEndpointDto): MonitoredEndpointDto {
        val result = endpointService.createEndpoint(
                endpointDto.name,
                endpointDto.url,
                endpointDto.monitoringInterval,
                endpointDto.owners
        )

        when (result) {
            is EndpointResult.Success -> return MonitoredEndpointDto.fromModel(result.monitoringEndpoint)
            is EndpointResult.Error -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, result.errorMessage)
        }
    }

}
