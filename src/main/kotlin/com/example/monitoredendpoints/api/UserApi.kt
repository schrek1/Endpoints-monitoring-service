package com.example.monitoredendpoints.api

import com.example.monitoredendpoints.api.dto.*
import com.example.monitoredendpoints.response.*
import com.example.monitoredendpoints.service.*
import io.swagger.annotations.*
import org.springframework.http.*
import org.springframework.validation.annotation.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import java.net.http.*
import javax.validation.*

@RestController
@RequestMapping("/api/v1/user")
@Validated
class UserApi(
        private val userService: UserService,
        private val endpointService: EndpointService
) {

    @PostMapping
    fun createUser(@Valid @RequestBody userDto: CreateUserDto): String =
            when (val result = userService.createUser(userDto.name, userDto.email)) {
                is UserResult.Success -> result.user.token
                is UserResult.Error -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, result.message)
            }


    @PostMapping("{userId}/endpoint/{endpointId}")
    fun addEndpoint(@PathVariable userId: String, @PathVariable endpointId: String): ResponseEntity<Any> =
            when (val result = endpointService.assignEndpoint(endpointId, userId)) {
                is EndpointResult.Success -> ResponseEntity.ok().build()
                is EndpointResult.Error -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, result.errorMessage)
            }

    @DeleteMapping("{userId}/endpoint/{endpointId}")
    fun removeEndpoint(@PathVariable userId: String, @PathVariable endpointId: String): ResponseEntity<Any> =
            when (val result = endpointService.removeEndpointAssign(endpointId, userId)) {
                is EndpointResult.Success -> ResponseEntity.ok().build()
                is EndpointResult.Error -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, result.errorMessage)
            }

    @GetMapping
    fun getUsers(): List<UserDto> = userService.getUsers().asSequence()
            .map { UserDto.fromModel(it) }
            .toList()
}