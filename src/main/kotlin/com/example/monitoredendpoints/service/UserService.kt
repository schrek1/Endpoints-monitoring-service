package com.example.monitoredendpoints.service

import com.example.monitoredendpoints.model.*
import com.example.monitoredendpoints.repository.*
import com.example.monitoredendpoints.response.*
import com.example.monitoredendpoints.utils.*
import org.springframework.stereotype.*
import java.util.*
import javax.transaction.*


@Service
@Transactional
class UserService(
        private val hashGenerator: HashGenerator,
        private val userRepo: UserRepository
) {

    fun createUser(name: String, email: String): UserResult {
        return try {
            val user = userRepo.save(User(Utils.getNullUUID(), name, email, hashGenerator.generateHash(name + email)))
            UserResult.Success(user)
        } catch (ex: Exception) {
            UserResult.Error.NotCreated()
        }
    }

    fun getUserById(id: String): UserResult {
        val optional = userRepo.findById(UUID.fromString(id))
        return if (optional.isPresent) {
            UserResult.Success(optional.get())
        } else {
            UserResult.Error.NotFound(userId = id)
        }
    }

    fun getUsers(): MutableList<User> = userRepo.findAll()

    fun existsUserWithToken(token: String) = userRepo.existsByToken(token)

    fun getUserByToken(token: String): UserResult {
        val optional = userRepo.findByToken(token)
        return if (optional.isPresent) {
            UserResult.Success(optional.get())
        } else {
            UserResult.Error.NotFound(message = "User with token=$token not found")
        }
    }

}