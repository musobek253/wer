package com.musobek.wer.auth.Controller

import com.musobek.wer.auth.dto.AuthenticationRequest
import com.musobek.wer.auth.dto.AuthenticationResponse
import com.musobek.wer.auth.dto.RegisterRequest
import com.musobek.wer.auth.service.AuthenticationService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.IOException

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.register(request))
    }

    @PostMapping("/authentication")
    fun authentication(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }

    @PostMapping("/refresh-token")
    @Throws(IOException::class)
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        authenticationService.refreshToken(request, response)
    }

    @GetMapping("/validateToken")
    fun validateToken(@RequestParam("token") token: String): Boolean {
        return authenticationService.validateToken(token)
    }
}