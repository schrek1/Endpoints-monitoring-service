package com.example.monitoredendpoints.repository

import com.example.monitoredendpoints.model.*
import com.example.monitoredendpoints.utils.*
import junit.framework.Assert.*
import org.junit.*
import org.junit.runner.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.context.*
import org.springframework.test.context.junit4.*
import java.time.*

@RunWith(SpringRunner::class)
@SpringBootTest
class MonitoringRepoTest {

    @Autowired
    private lateinit var monitoringRepo: MonitoringRepository

    @Autowired
    private lateinit var endpointRepo: EndpointRepository

    @Test
    fun testGetOldest() {
        val timeNow = LocalDateTime.now()
        val oldestTime = timeNow.minusSeconds(1)

        val endpoint1 = endpointRepo.save(MonitoredEndpoint(Utils.getNullUUID(), "e1", "url", timeNow, timeNow, 1, mutableListOf()))
        val endpoint2 = endpointRepo.save(MonitoredEndpoint(Utils.getNullUUID(), "e2", "url", timeNow, timeNow, 1, mutableListOf()))

        with(monitoringRepo) {
            save(MonitoringResult(Utils.getNullUUID(), oldestTime, 200, "body", true, endpoint1))
            save(MonitoringResult(Utils.getNullUUID(), timeNow, 200, "body", true, endpoint1))
            save(MonitoringResult(Utils.getNullUUID(), timeNow, 200, "body", true, endpoint1))

            save(MonitoringResult(Utils.getNullUUID(), oldestTime, 100, "oldest", false, endpoint2))
            save(MonitoringResult(Utils.getNullUUID(), timeNow, 200, "body", true, endpoint2))
            save(MonitoringResult(Utils.getNullUUID(), timeNow, 200, "body", true, endpoint2))
        }

        val oldestResult = monitoringRepo.getOldest(endpoint2).get()

        assertEquals(oldestResult.statusCode, 100)
        assertEquals(oldestResult.responseBody, "oldest")
        assertFalse(oldestResult.isSuccessful)
    }
}