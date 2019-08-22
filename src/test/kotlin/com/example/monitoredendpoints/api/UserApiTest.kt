package com.example.monitoredendpoints.api

import com.example.monitoredendpoints.api.dto.*
import com.example.monitoredendpoints.model.*
import com.example.monitoredendpoints.response.*
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
import org.springframework.http.*
import org.springframework.test.context.junit4.*
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.*
import org.mockito.Mockito.`when` as mockitoWhen

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class UserApiTest{

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var endpointService: EndpointService

    @InjectMocks
    private lateinit var userApi: UserApi

    private val testUser = CreateUserDto("test", "email@domain.com")


    @Before
    fun beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(userApi).build()
    }

    @Test
    fun createUserTest() {

        mockitoWhen(userService.createUser(anyString(), anyString()))
                .thenReturn(UserResult.Success(User(Utils.getNullUUID(), testUser.name, testUser.email, "test-token")))


        mockMvc.perform(post("/api/v1/user")
                .content(mapper.writeValueAsString(testUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun createUserBadNameTest() {
        mockMvc.perform(post("/api/v1/user")
                .content("{\"name\":\"\", \"email\":\"${testUser.email}\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun createUserBadEmailTest() {
        mockMvc.perform(post("/api/v1/user")
                .content("{\"name\":\"${testUser.name}\", \"email\":\"aaa\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }
}