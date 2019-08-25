package com.example.monitoredendpoints.service

import com.example.monitoredendpoints.model.*
import com.example.monitoredendpoints.response.*
import com.example.monitoredendpoints.utils.*
import kotlinx.coroutines.*
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.*
import org.mockito.*
import org.mockito.Mockito.*
import org.springframework.boot.test.context.*
import org.springframework.test.context.junit4.*
import java.time.*
import org.mockito.Mockito.`when` as mockitoWhen


@RunWith(SpringRunner::class)
@SpringBootTest
class SchedulerServiceTest {

    @Mock
    private lateinit var endpointService: EndpointService

    @Mock
    private lateinit var monitoringService: MonitoringService

    @Mock
    private lateinit var senderService: SenderService

    @InjectMocks
    private lateinit var schedulerService: SchedulerService

    private val monitoringInterval = 1
    private val monitoredEndpoint = MonitoredEndpoint(Utils.getNullUUID(), "e1", "url", LocalDateTime.now().minusSeconds(monitoringInterval.toLong()), LocalDateTime.now().minusSeconds(monitoringInterval.toLong()), monitoringInterval, mutableListOf(User(Utils.getNullUUID(), "username", "email", "token")))

    @Test
    @Ignore("not waiting for corutine call")
    fun testPerformMonitoringWithSuccessResult() {
        runBlocking {
            val statusCodeArg = 200
            val responseMessageArg = "response"

            mockitoWhen(endpointService.getAllEndpoints()).thenReturn(mutableListOf(monitoredEndpoint))
            mockitoWhen(senderService.sendRequest(kEq(monitoredEndpoint.url))).thenReturn(SenderResult.Success(responseMessageArg, statusCodeArg))

            schedulerService.performMonitoringEndpoints()

            delay(1000)

            val captCheckTime = ArgumentCaptor.forClass(LocalDateTime::class.java)
            val captStatusCode = ArgumentCaptor.forClass(Int::class.java)
            val captResponseBody = ArgumentCaptor.forClass(String::class.java)
            val captMonitoredEndpoint = ArgumentCaptor.forClass(MonitoredEndpoint::class.java)
            val captIsSuccessful = ArgumentCaptor.forClass(Boolean::class.java)

            verify(monitoringService, times(1)).appendMonitoringResult(captCheckTime.capture(), captStatusCode.capture(), captResponseBody.capture(), captMonitoredEndpoint.capture(), captIsSuccessful.capture())

            assertTrue(monitoredEndpoint.lastCheck < captCheckTime.value)
            assertEquals(captStatusCode.value, statusCodeArg)
            assertEquals(captResponseBody.value, responseMessageArg)
            assertEquals(captMonitoredEndpoint.value, monitoredEndpoint)
            assertTrue(captIsSuccessful.value)

            verify(endpointService, times(1)).updateEndpoint(captMonitoredEndpoint.capture())

            assertEquals(captMonitoredEndpoint.value.lastCheck, captCheckTime.value)
        }
    }

    @Test
    @Ignore("not waiting for corutine call")
    fun testPerformMonitoringWithBadResult() {
        runBlocking {
            val statusCodeArg = 200
            val responseMessageArg = "response"

            mockitoWhen(endpointService.getAllEndpoints()).thenReturn(mutableListOf(monitoredEndpoint))
            mockitoWhen(senderService.sendRequest(kEq(monitoredEndpoint.url))).thenReturn(SenderResult.Error(responseMessageArg, statusCodeArg))

            schedulerService.performMonitoringEndpoints()

            delay(1000)

            val captCheckTime = ArgumentCaptor.forClass(LocalDateTime::class.java)
            val captStatusCode = ArgumentCaptor.forClass(Int::class.java)
            val captResponseBody = ArgumentCaptor.forClass(String::class.java)
            val captMonitoredEndpoint = ArgumentCaptor.forClass(MonitoredEndpoint::class.java)
            val captIsSuccessful = ArgumentCaptor.forClass(Boolean::class.java)

            verify(monitoringService, times(1)).appendMonitoringResult(captCheckTime.capture(), captStatusCode.capture(), captResponseBody.capture(), captMonitoredEndpoint.capture(), captIsSuccessful.capture())

            assertTrue(monitoredEndpoint.lastCheck < captCheckTime.value)
            assertEquals(captStatusCode.value, statusCodeArg)
            assertEquals(captResponseBody.value, responseMessageArg)
            assertEquals(captMonitoredEndpoint.value, monitoredEndpoint)
            assertTrue(captIsSuccessful.value)

            verify(endpointService, times(1)).updateEndpoint(captMonitoredEndpoint.capture())

            assertEquals(captMonitoredEndpoint.value.lastCheck, captCheckTime.value)
        }
    }
}