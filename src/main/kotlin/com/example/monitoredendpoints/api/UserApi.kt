package com.example.monitoredendpoints.api

import com.example.monitoredendpoints.api.dto.*
import com.example.monitoredendpoints.response.*
import com.example.monitoredendpoints.service.*
import org.springframework.http.*
import org.springframework.validation.annotation.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import javax.validation.*

@RestController
@RequestMapping("/api/v1/user")
@Validated
class UserApi(
        private val userService: UserService,
        private val endpointService: EndpointService
) {

    @PostMapping
    fun createUser(@Valid @RequestBody userDto: CreateUserDto): String {
        val result = userService.createUser(userDto.name, userDto.email)

        when (result) {
            is UserResult.Success -> return result.user.token
            is UserResult.Error -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, result.message)
        }
    }

    @PostMapping("{userId}/endpoint/{endpointId}")
    fun addEndpoint(@PathVariable userId: String, @PathVariable endpointId: String) {
        endpointService.assignEndpoint(endpointId, userId)
    }

    @DeleteMapping("{userId}/endpoint/{endpointId}")
    fun removeEndpoint(@PathVariable userId: String, @PathVariable endpointId: String) {
        endpointService.removeEndpointAssign(endpointId, userId)
    }

    @GetMapping
    fun getUsers() = userService.getUsers().asSequence()
            .map { UserDto.fromModel(it) }
            .toList()
}