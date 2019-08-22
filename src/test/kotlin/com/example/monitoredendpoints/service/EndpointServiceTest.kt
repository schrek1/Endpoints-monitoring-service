package com.example.monitoredendpoints.service

import com.example.monitoredendpoints.model.*
import com.example.monitoredendpoints.repository.*
import com.example.monitoredendpoints.response.*
import com.example.monitoredendpoints.utils.*
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.*
import org.mockito.*
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.springframework.boot.test.context.*
import org.springframework.test.context.junit4.*
import java.time.*
import java.util.*
import org.mockito.Mockito.`when` as mockitoWhen

@RunWith(SpringRunner::class)
@SpringBootTest
class EndpointServiceTest {

    @Mock
    private lateinit var endpointRepo: EndpointRepository

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var endpointService: EndpointService

    private val uuidStr = Utils.getNullUUID().toString()
    private val monitoredEndpoint = MonitoredEndpoint(Utils.getNullUUID(), "e1", "url", LocalDateTime.now(), LocalDateTime.now(), 1, mutableListOf(User(Utils.getNullUUID(), "username", "email", "token")))
    private val user = User(Utils.getNullUUID(), "name", "email", "token")

    @Test
    fun getEndpointByIdSuccess() {
        val arg = monitoredEndpoint

        mockitoWhen(endpointRepo.findById(any()))
                .thenReturn(Optional.of(arg))

        val endpointResult = endpointService.getEndpointById(arg.id.toString())

        if (endpointResult is EndpointResult.Success) {
            val result = endpointResult.monitoringEndpoint

            assertEquals(result.id, arg.id)
            assertEquals(result.name, arg.name)
            assertEquals(result.url, arg.url)
            assertEquals(result.creation, arg.creation)
            assertEquals(result.lastCheck, arg.lastCheck)
            assertEquals(result.monitoringInterval, arg.monitoringInterval)
            assertTrue(result.owners == arg.owners)
            assertTrue(result.monitoringResults == emptyList<MonitoringResult>())
        } else {
            fail("endpointResult is unexpected type")
        }
    }

    @Test
    fun getEndpointByIdNotFound() {

        mockitoWhen(endpointRepo.findById(any()))
                .thenReturn(Optional.empty())

        val endpointResult = endpointService.getEndpointById(uuidStr)

        if (endpointResult !is EndpointResult.Error.NotFound) {
            fail("endpointResult is unexpected type")
        }
    }

    @Test
    fun assignEndpointSuccess() {

        mockitoWhen(endpointRepo.findById(any())).thenReturn(Optional.of(monitoredEndpoint))
        mockitoWhen(userService.getUserById(anyString())).thenReturn(UserResult.Success(user))
        mockitoWhen(endpointRepo.save(any<MonitoredEndpoint>())).thenReturn(monitoredEndpoint)

        val endpointResult = endpointService.assignEndpoint(uuidStr, "uId")

        if (endpointResult !is EndpointResult.Success) {
            fail("endpoint is not EndpointResult.Success")
        }
    }

    @Test
    fun assignEndpointUserNotFound() {

        mockitoWhen(endpointRepo.findById(any())).thenReturn(Optional.of(monitoredEndpoint))
        mockitoWhen(userService.getUserById(anyString())).thenReturn(UserResult.Error.NotFound())

        val endpointResult = endpointService.assignEndpoint(uuidStr, "uId")

        if (endpointResult !is EndpointResult.Error.NotAssigned) {
            fail("endpoint is not EndpointResult.Error.NotAssigned")
        }
    }

    @Test
    fun assignEndpointUserNotAssigned() {

        mockitoWhen(endpointRepo.findById(any())).thenReturn(Optional.empty())

        val endpointResult = endpointService.assignEndpoint(uuidStr, "uId")

        if (endpointResult !is EndpointResult.Error.NotAssigned) {
            fail("endpoint is not EndpointResult.Error.NotAssigned")
        }
    }

    @Test
    fun removeEndpointSuccess() {

        mockitoWhen(endpointRepo.findById(any())).thenReturn(Optional.of(monitoredEndpoint))
        mockitoWhen(userService.getUserById(anyString())).thenReturn(UserResult.Success(user))
        mockitoWhen(endpointRepo.save(any<MonitoredEndpoint>())).thenReturn(monitoredEndpoint)

        val endpointResult = endpointService.removeEndpointAssign(uuidStr, "uId")

        if (endpointResult !is EndpointResult.Success) {
            fail("endpoint is not EndpointResult.Success")
        }
    }

    @Test
    fun removeEndpointUserNotFound() {

        mockitoWhen(endpointRepo.findById(any())).thenReturn(Optional.of(monitoredEndpoint))
        mockitoWhen(userService.getUserById(anyString())).thenReturn(UserResult.Error.NotFound())

        val endpointResult = endpointService.removeEndpointAssign(uuidStr, "uId")

        if (endpointResult !is EndpointResult.Error.NotAssigned) {
            fail("endpoint is not EndpointResult.Error.NotAssigned")
        }
    }

    @Test
    fun removeEndpointUserNotAssigned() {

        mockitoWhen(endpointRepo.findById(any())).thenReturn(Optional.empty())

        val endpointResult = endpointService.removeEndpointAssign(uuidStr, "uId")

        if (endpointResult !is EndpointResult.Error.NotFound) {
            fail("endpoint is not EndpointResult.Error.NotFound")
        }
    }
}