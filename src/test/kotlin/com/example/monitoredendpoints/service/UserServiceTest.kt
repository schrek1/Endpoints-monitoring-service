package com.example.monitoredendpoints.service

import com.example.monitoredendpoints.model.*
import com.example.monitoredendpoints.repository.*
import com.example.monitoredendpoints.response.*
import com.example.monitoredendpoints.utils.*
import junit.framework.Assert.*
import org.junit.*
import org.junit.runner.*
import org.mockito.*
import org.mockito.ArgumentMatchers.*
import org.springframework.boot.test.context.*
import org.springframework.test.context.junit4.*
import java.util.*
import org.mockito.Mockito.`when` as mockitoWhen

@RunWith(SpringRunner::class)
@SpringBootTest
class UserServiceTest {

    @Spy
    private val hashGenerator: HashGenerator = HashGenerator()

    @Mock
    private lateinit var userRepo: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    private val uuidString = Utils.getNullUUID().toString()
    private val user = User(id = Utils.getNullUUID(), name = "", email = "", token = "")

    @Test
    fun testCreateUserSuccess() {
        mockitoWhen(userRepo.save<User>(kAny())).thenReturn(user)

        val result = userService.createUser(user.name, user.email)

        if (result is UserResult.Success) {
            assertEquals(result.user, user)
        } else {
            Assert.fail("method result is not UserResult.Success")
        }
    }

    @Test
    fun testCreateUserError() {
        mockitoWhen(userRepo.save<User>(kAny())).thenThrow(RuntimeException())

        val result = userService.createUser(user.name, user.email)

        if (result !is UserResult.Error.NotCreated) {
            Assert.fail("method result is not UserResult.Error.NotCreated")
        }
    }

    @Test
    fun testGetUserByIdSuccess() {
        mockitoWhen(userRepo.findById(kAny())).thenReturn(Optional.of(user))

        val result = userService.getUserById(uuidString)

        if (result is UserResult.Success) {
            assertEquals(result.user, user)
        } else {
            Assert.fail("method result is not UserResult.Success")
        }
    }


    @Test
    fun testGetUserByIdUserNotFound() {
        mockitoWhen(userRepo.findById(kAny())).thenReturn(Optional.empty())

        val result = userService.getUserById(uuidString)

        if (result !is UserResult.Error.NotFound) {
            Assert.fail("method result is not UserResult.Error.NotFound")
        }
    }

    @Test
    fun testGetUser() {
        val argList = listOf(user)
        mockitoWhen(userRepo.findAll()).thenReturn(argList)

        val resultList = userService.getUsers()

        assertEquals(resultList, argList)
    }


    @Test
    fun testExistsUserWithToken() {
        val id = "id"

        mockitoWhen(userRepo.existsByToken(kEq(id))).thenReturn(true)
        assertTrue(userService.existsUserWithToken(id))

        mockitoWhen(userRepo.existsByToken(kEq(id))).thenReturn(false)
        assertFalse(userService.existsUserWithToken(id))
    }

    @Test
    fun testGetUSerByTokenSuccess() {
        val token = "token"
        mockitoWhen(userRepo.findByToken(kEq(token))).thenReturn(Optional.of(user))

        val result = userService.getUserByToken(token)

        if (result is UserResult.Success) {
            assertEquals(result.user, user)
        } else {
            Assert.fail("method result is not UserResult.Success")
        }
    }

    @Test
    fun testGetUSerByTokenUserNotFound() {
        mockitoWhen(userRepo.findByToken(kAny())).thenReturn(Optional.empty())

        val result = userService.getUserByToken(uuidString)

        if (result !is UserResult.Error.NotFound) {
            Assert.fail("method result is not UserResult.Error.NotFound")
        }
    }
}