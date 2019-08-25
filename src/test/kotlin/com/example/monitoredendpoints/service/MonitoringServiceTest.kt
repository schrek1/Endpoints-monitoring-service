package com.example.monitoredendpoints.service

import com.example.monitoredendpoints.config.Properties
import com.example.monitoredendpoints.model.*
import com.example.monitoredendpoints.repository.*
import com.example.monitoredendpoints.utils.*
import org.junit.*
import org.junit.runner.*
import org.mockito.*
import org.mockito.Mockito.*
import org.springframework.boot.test.context.*
import org.springframework.test.context.junit4.*
import java.time.*
import java.util.*
import org.mockito.Mockito.`when` as mockitoWhen

@RunWith(SpringRunner::class)
@SpringBootTest
class MonitoringServiceTest {

    @Mock
    private lateinit var monitoringRepo: MonitoringRepository

    @Spy
    private val properties: Properties = Properties()

    @InjectMocks
    private lateinit var monitoringService: MonitoringService

    private val monitoredEndpoint = MonitoredEndpoint(
            id = Utils.getNullUUID(),
            name = "",
            url = "",
            creation = LocalDateTime.now(),
            lastCheck = LocalDateTime.now(),
            monitoringInterval = 0,
            owners = mutableListOf())

    private val monitoringResult: MonitoringResult = MonitoringResult(
            id = Utils.getNullUUID(),
            checkTime = LocalDateTime.now(),
            statusCode = 0,
            responseBody = "",
            isSuccessful = false,
            monitoredEndpoint = monitoredEndpoint
    )
    private val SAVED_RESULTS: Int = 6
    private val MAX_RESULT_COUNT: Int = 3
    private val DELETE_CALLS_COUNT: Int = SAVED_RESULTS - MAX_RESULT_COUNT + 1

    private var remainResults: Int = SAVED_RESULTS

    @Before
    fun beforeEach() {
        remainResults = SAVED_RESULTS
    }

    @Test
    fun testAppendMonitoringResultMoreCount() {
        properties.maxMonitoringPersistence = MAX_RESULT_COUNT
        mockitoWhen(monitoringRepo.countByMonitoredEndpoint(kEq(monitoredEndpoint))).thenAnswer { return@thenAnswer remainResults-- }
        mockitoWhen(monitoringRepo.getOldest(kEq(monitoredEndpoint))).thenReturn(Optional.of(monitoringResult))

        monitoringService.appendMonitoringResult(LocalDateTime.now(), 0, "", monitoredEndpoint, true)

        verify(monitoringRepo, times(DELETE_CALLS_COUNT)).delete(kEq(monitoringResult))
        verify(monitoringRepo, times(1)).save(kAny())
    }

    @Test
    fun testAppendMonitoringResultEqualsCount() {
        properties.maxMonitoringPersistence = MAX_RESULT_COUNT
        remainResults = MAX_RESULT_COUNT
        mockitoWhen(monitoringRepo.countByMonitoredEndpoint(kEq(monitoredEndpoint))).thenAnswer { return@thenAnswer remainResults-- }
        mockitoWhen(monitoringRepo.getOldest(kEq(monitoredEndpoint))).thenReturn(Optional.of(monitoringResult))

        monitoringService.appendMonitoringResult(LocalDateTime.now(), 0, "", monitoredEndpoint, true)

        verify(monitoringRepo, times(1)).delete(kEq(monitoringResult))
        verify(monitoringRepo, times(1)).save(kAny())
    }

    @Test
    fun testAppendMonitoringResultLessCount() {
        properties.maxMonitoringPersistence = MAX_RESULT_COUNT
        remainResults = MAX_RESULT_COUNT - 1
        mockitoWhen(monitoringRepo.countByMonitoredEndpoint(kEq(monitoredEndpoint))).thenAnswer { return@thenAnswer remainResults-- }
        mockitoWhen(monitoringRepo.getOldest(kEq(monitoredEndpoint))).thenReturn(Optional.of(monitoringResult))

        monitoringService.appendMonitoringResult(LocalDateTime.now(), 0, "", monitoredEndpoint, true)

        verify(monitoringRepo, times(0)).delete(kEq(monitoringResult))
        verify(monitoringRepo, times(1)).save(kAny())
    }
}