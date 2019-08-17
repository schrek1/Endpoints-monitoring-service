package com.example.monitoredendpoints.repository

import com.example.monitoredendpoints.model.*
import org.springframework.data.jpa.repository.*
import java.util.*

interface EndpointRepository : JpaRepository<MonitoredEndpoint, UUID> {

    fun findAllByOwners(user: User):List<MonitoredEndpoint>
}