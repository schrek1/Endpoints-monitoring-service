package com.example.monitoredendpoints.repository

import com.example.monitoredendpoints.model.*
import org.springframework.data.jpa.repository.*
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    fun existsByToken(token: String): Boolean

    fun findByToken(token: String): Optional<User>
}