package com.example.monitoredendpoints.repository

import com.example.monitoredendpoints.model.*
import org.springframework.data.jpa.repository.*
import org.springframework.data.repository.query.*
import java.util.*

interface MonitoringRepository : JpaRepository<MonitoringResult, UUID> {

    fun countByMonitoredEndpoint(monitoredEndpoint: MonitoredEndpoint): Int

    @Query("SELECT result FROM  MonitoringResult result WHERE result.checkTime = (SELECT MIN(m.checkTime) FROM  MonitoringResult m WHERE m.monitoredEndpoint.id = :#{#endpoint.id}) AND result.monitoredEndpoint.id = :#{#endpoint.id}")
    fun getOldest(@Param("endpoint") monitoredEndpoint: MonitoredEndpoint): Optional<MonitoringResult>

}
