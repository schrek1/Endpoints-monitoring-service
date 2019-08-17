package com.example.monitoredendpoints.response

import com.example.monitoredendpoints.model.*

sealed class EndpointResult {
    class Success(val monitoringEndpoint: MonitoredEndpoint) : EndpointResult()
    sealed class Error(val message: String) : EndpointResult() {
        class NotCreated(
                private val reason: String,
                message: String = "Endpoint not created because: $reason"
        ) : Error(message)

        class NotFound(
                private val endpointId: String = "",
                message: String = "Monitored endpoint with id=$endpointId not found"
        ) : Error(message)

        class NotAssigned(
                private val reason: String,
                message: String = "Endpoint isn't assigned because: $reason"
        ) : Error(message)
    }
}