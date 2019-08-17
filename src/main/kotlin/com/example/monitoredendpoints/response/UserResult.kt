package com.example.monitoredendpoints.response

import com.example.monitoredendpoints.model.*

sealed class UserResult {
    class Success(val user: User) : UserResult()
    sealed class Error(val message: String) : UserResult() {
        class NotCreated(
                message: String = "User not created"
        ) : Error(message)

        class NotFound(
                private val userId: String = "",
                message: String = "User with id=$userId not found"
        ) : Error(message)
    }
}