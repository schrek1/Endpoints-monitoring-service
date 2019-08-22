package com.example.monitoredendpoints.service

import com.example.monitoredendpoints.response.*
import kotlinx.coroutines.*
import mu.*
import org.springframework.scheduling.annotation.*
import org.springframework.stereotype.*
import java.time.*


@Service
class SchedulerService(
        private val endpointService: EndpointService,
        private val monitoringService: MonitoringService,
        private val senderService: SenderService
) {
    private val log = KotlinLogging.logger {}

    @Scheduled(fixedRate = 500)
    fun performMonitoringEndpoints() = runBlocking {
        val timeNow = LocalDateTime.now()
        log.info("Monitoring at $timeNow")
        endpointService.getAllEndpoints().asSequence()
                .forEach { endpoint ->
                    val timeAfterLastCheck = Duration.between(endpoint.lastCheck, timeNow)

                    if (timeAfterLastCheck.toSeconds() >= endpoint.monitoringInterval) {
                        log.info("Perform monitoring ${endpoint.name}")

                        launch {
                            when (val result = senderService.sendRequest(endpoint.url)) {
                                is SenderResult.Success -> monitoringService.appendMonitoringResult(
                                        checkTime = timeNow,
                                        statusCode = result.statusCode,
                                        responseBody = result.response,
                                        monitoredEndpoint = endpoint,
                                        isSuccessful = true
                                )

                                is SenderResult.Error -> monitoringService.appendMonitoringResult(
                                        checkTime = timeNow,
                                        statusCode = result.statusCode,
                                        responseBody = result.errorMessage,
                                        monitoredEndpoint = endpoint,
                                        isSuccessful = false
                                )
                            }
                        }

                        endpoint.lastCheck = timeNow
                        endpointService.updateEndpoint(endpoint)
                    }
                }
    }
}