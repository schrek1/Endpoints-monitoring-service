package com.example.monitoredendpoints.api.dto

import com.example.monitoredendpoints.model.*

data class UserDto(
        val id: String,

        val name: String,

        val email: String,

        val endpoints: List<String>
) {
    companion object {
        fun fromModel(model: User) = UserDto(
                id = model.id.toString(),
                name = model.name,
                email = model.email,
                endpoints = model.monitoredEndpoints.asSequence()
                        .map { it.id.toString() }
                        .toList()
        )
    }
}