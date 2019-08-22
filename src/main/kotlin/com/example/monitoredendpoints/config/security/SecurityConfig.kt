package com.example.monitoredendpoints.config.security

import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import org.springframework.security.config.annotation.authentication.builders.*
import org.springframework.security.config.annotation.method.configuration.*
import org.springframework.security.config.annotation.web.builders.*
import org.springframework.security.config.annotation.web.configuration.*
import org.springframework.security.config.http.*
import org.springframework.security.web.authentication.*
import org.springframework.security.web.util.matcher.*


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