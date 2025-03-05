package com.musobek.wer.auth.config

import com.musobek.wer.auth.model.enam.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val  authenticationProvider: AuthenticationProvider,
    private val jwtFilter: JwtAuthenticationFilter,
    private val  logoutHandler: LogoutHandler,)
{

    private companion object {
        val WHITE_LIST_URL = arrayOf(
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
        )
    }
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // CSRF himoyasini o'chirish (JWT bilan ishlash uchun)
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(*WHITE_LIST_URL).permitAll()
                    .requestMatchers("/api/v1/mengment/**").hasAnyRole(Role.ADMIN.name, Role.MANAGER.name)
                    .anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .logout { logout ->
                logout.logoutUrl("/api/v1/auth/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler { _, _, _ -> SecurityContextHolder.clearContext() }
            }

        return http.build()
    }



}



