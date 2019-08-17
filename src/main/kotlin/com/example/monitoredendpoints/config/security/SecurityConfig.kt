package com.example.monitoredendpoints.config.security

import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    companion object {
        private val PROTECTED_URLS = OrRequestMatcher(
                AntPathRequestMatcher("/api/v1/monitoring/**")
        )
    }

    @Autowired
    private lateinit var provider: AuthenticationProvider


    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(provider)
    }

    override fun configure(http: HttpSecurity) {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(getAuthenticationFilter(PROTECTED_URLS), AnonymousAuthenticationFilter::class.java)
                .authorizeRequests()
                .requestMatchers(PROTECTED_URLS)
                .authenticated()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
    }


    private fun getAuthenticationFilter(protectedUrls: OrRequestMatcher): AuthenticationFilter {
        return AuthenticationFilter(protectedUrls).also { it.setAuthenticationManager(authenticationManager()) }
    }

}