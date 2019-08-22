package com.example.monitoredendpoints.service

import com.example.monitoredendpoints.config.*
import com.example.monitoredendpoints.model.*
import com.example.monitoredendpoints.repository.*
import com.example.monitoredendpoints.utils.*
import org.springframework.stereotype.*
import java.time.*
import java.util.function.Consumer
import javax.transaction.*

@Service
@Transactional
class MonitoringService(
        private val monitoringRepo: MonitoringRepository,
        private val properties: Properties
) {

    fun appendMonitoringResult(checkTime: LocalDateTime, statusCode: Int, responseBody: String, monitoredEndpoint: MonitoredEndpoint, isSuccessful: Boolean) {
        checkMonitoringResultCount(monitoredEndpoint)
        monitoringRepo.save(MonitoringResult(
                id = Utils.getNullUUID(),
                checkTime = checkTime,
                statusCode = statusCode,
                responseBody = responseBody,
                monitoredEndpoint = monitoredEndpoint,
                isSuccessful = isSuccessful)
        )
    }

    private fun checkMonitoringResultCount(monitoredEndpoint: MonitoredEndpoint) {
        while (getMonitoringResultCount(monitoredEndpoint) >= properties.maxMonitoringPersistence) {
            monitoringRepo.getOldest(monitoredEndpoint).ifPresent { monitoringRepo.delete(it) }
        }
    }

    private fun getMonitoringResultCount(monitoredEndpoint: MonitoredEndpoint) = monitoringRepo.countByMonitoredEndpoint(monitoredEndpoint)

}
