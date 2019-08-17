package com.example.monitoredendpoints.api.dto

import javax.validation.constraints.*

data class CreateUserDto(
        @NotBlank(message = "name must be filled")
        val name: String,

        @NotBlank(message = "email must be filled")
        val email: String
)