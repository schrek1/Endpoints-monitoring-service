package com.example.monitoredendpoints.response

import com.example.monitoredendpoints.model.*

sealed class EndpointResult {
    class Success(val monitoringEndpoint: MonitoredEndpoint) : EndpointResult()
    sealed class Error(val errorMessage: String) : EndpointResult() {
        class NotCreated(
                private val reason: String,
                errorMessage: String = "Endpoint not created because: $reason"
        ) : Error(errorMessage)

        class NotFound(
                private val endpointId: String = "",
                errorMessage: String = "Monitored endpoint with id=$endpointId not found"
        ) : Error(errorMessage)

        class NotAssigned(
                private val reason: String,
                errorMessage: String = "Endpoint isn't assigned because: $reason"
        ) : Error(errorMessage)
    }
}