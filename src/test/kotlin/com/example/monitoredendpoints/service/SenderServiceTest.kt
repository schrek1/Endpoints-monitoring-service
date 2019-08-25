package com.example.monitoredendpoints.service

import com.example.monitoredendpoints.response.*
import com.example.monitoredendpoints.utils.*
import org.junit.*
import kotlinx.coroutines.*
import org.junit.Assert.*
import org.junit.runner.*
import org.mockito.*
import org.mockito.Mockito.*
import org.springframework.boot.test.context.*
import org.springframework.http.*
import org.springframework.test.context.junit4.*
import org.springframework.web.client.*
import java.lang.RuntimeException
import org.mockito.Mockito.`when` as mockitoWhen

@RunWith(SpringRunner::class)
@SpringBootTest
class SenderServiceTest {

    @Mock
    private lateinit var restClient: RestTemplate

    @InjectMocks
    private lateinit var senderService: SenderService


    @Test
    fun testSendRequestSuccess() {
        val responseBody = "response_body"
        mockitoWhen(restClient.getForEntity<String>(kAny<String>(), kAny<Class<String>>()))
                .thenReturn(ResponseEntity.ok(responseBody))

        runBlocking {
            val result = senderService.sendRequest("url")

            if (result is SenderResult.Success) {
                assertEquals(result.response, responseBody)
                assertEquals(result.statusCode, HttpStatus.OK.value())
            } else {
                fail("method result is not SenderResult.Success")
            }
        }
    }

    @Test
    fun testSendRequestBadResponse() {
        val responseBody = "response_body"
        mockitoWhen(restClient.getForEntity<String>(kAny<String>(), kAny<Class<String>>()))
                .thenReturn(ResponseEntity(responseBody, HttpStatus.INTERNAL_SERVER_ERROR))

        runBlocking {
            val result = senderService.sendRequest("url")

            if (result is SenderResult.Error) {
                assertEquals(result.errorMessage, responseBody)
                assertEquals(result.statusCode, HttpStatus.INTERNAL_SERVER_ERROR.value())
            } else {
                fail("method result is not SenderResult.Error")
            }
        }
    }

    @Test
    fun testSendRequestException() {
        val errorMessage = "error_message"
        mockitoWhen(restClient.getForEntity<String>(kAny<String>(), kAny<Class<String>>()))
                .thenThrow(RuntimeException(errorMessage))

        runBlocking {
            val result = senderService.sendRequest("url")

            if (result is SenderResult.Error) {
                assertEquals(result.errorMessage, errorMessage)
                assertEquals(result.statusCode, -1)
            } else {
                fail("method result is not SenderResult.Error")
            }
        }
    }
}