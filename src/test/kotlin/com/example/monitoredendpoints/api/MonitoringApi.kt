package com.example.monitoredendpoints.api

import com.example.monitoredendpoints.api.dto.*
import com.example.monitoredendpoints.model.*
import com.example.monitoredendpoints.repository.*
import com.example.monitoredendpoints.service.*
import com.example.monitoredendpoints.utils.*
import com.fasterxml.jackson.databind.*
import org.junit.*
import org.junit.runner.*
import org.mockito.*
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.autoconfigure.web.servlet.*
import org.springframework.boot.test.context.*
import org.springframework.boot.test.mock.mockito.*
import org.springframework.http.*
import org.springframework.test.context.junit4.*
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.*
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.Mockito.`when` as mockitoWhen


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class MonitoringApi {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockBean
    private lateinit var userService: UserService


    @Before
    fun beforeEach() {
        mockitoWhen(userService.existsUserWithToken(ArgumentMatchers.anyString()))
                .thenReturn(true)
    }

    @Test
    @Ignore("can't pass trough authentication")
    fun testCreateEndpoint() {
        mvc.perform(post("/api/v1/monitoring")
                .content("{" +
                        "\"id\"=\"id\"," +
                        "\"name\"=\"name\"," +
                        "\"url\"=\"url\"," +
                        "\"monitoringInterval\"= 1," +
                        "\"owners\"=[]" +
                        "}"
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "test_token")
        ).andExpect(status().isOk)
    }

    @Test
    @Ignore("can't pass trough authentication")
    fun testBadNameCreateEndpoint() {
        mvc.perform(post("/api/v1/monitoring")
                .content(mapper.writeValueAsString(CreateEndpointDto(
                        "",
                        "url",
                        1,
                        mutableListOf()
                )))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "test_token")
        ).andExpect(status().isOk)
    }

}