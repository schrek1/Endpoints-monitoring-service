package com.example.monitoredendpoints.config.security

import org.springframework.security.authentication.*
import org.springframework.security.core.*
import org.springframework.security.core.context.*
import org.springframework.security.web.authentication.*
import org.springframework.security.web.util.matcher.*
import javax.servlet.*
import javax.servlet.http.*


class AuthenticationFilter(requiresAuth: RequestMatcher) : AbstractAuthenticationProcessingFilter(requiresAuth) {

    override fun attemptAuthentication(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse): Authentication {
        val token = httpServletRequest.getHeader("Authorization")?.removePrefix("Bearer")?.trim()
                ?: throw AuthenticationCredentialsNotFoundException("Authorization field in header is missing")
        return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(token, null))
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        SecurityContextHolder.getContext().authentication = authResult
        chain.doFilter(request, response)
    }
}