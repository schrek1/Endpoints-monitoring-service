package com.example.monitoredendpoints.api.dto

import javax.validation.constraints.*

data class CreateUserDto(
        @field:NotBlank(message = "name must be filled")
        val name: String,

        @field:Email(message = "email must be filled, and with correct format")
        val email: String
)