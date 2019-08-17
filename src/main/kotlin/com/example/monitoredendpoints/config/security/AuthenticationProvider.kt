package com.example.monitoredendpoints.config.security

import com.example.monitoredendpoints.service.*
import org.springframework.beans.factory.annotation.*
import org.springframework.security.authentication.dao.*
import org.springframework.stereotype.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.*


@Component
class AuthenticationProvider : AbstractUserDetailsAuthenticationProvider() {

    @Autowired
    lateinit var userService: UserService

    override fun additionalAuthenticationChecks(userDetails: UserDetails?, authentication: UsernamePasswordAuthenticationToken?) {
    }

    override fun retrieveUser(userName: String, usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken): UserDetails {
        val token = usernamePasswordAuthenticationToken.principal as String

        if (userService.existsUserWithToken(token)) {
            return User(token, token, listOf())
        } else {
            throw UsernameNotFoundException("Cannot find user with authentication token=$token")
        }

    }
}