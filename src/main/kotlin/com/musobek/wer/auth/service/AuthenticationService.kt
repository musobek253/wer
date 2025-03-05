package com.musobek.wer.auth.service


import com.fasterxml.jackson.databind.ObjectMapper
import com.musobek.wer.auth.dto.AuthenticationRequest
import com.musobek.wer.auth.dto.AuthenticationResponse
import com.musobek.wer.auth.dto.RegisterRequest
import com.musobek.wer.auth.model.Token
import com.musobek.wer.auth.model.User
import com.musobek.wer.auth.model.enam.TokenType
import com.musobek.wer.auth.repo.TokenRepository
import com.musobek.wer.auth.repo.UserRepository
import com.musobek.wer.auth.security.JWTService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JWTService,
    private val authenticationManager: AuthenticationManager,
    private val tokenRepository: TokenRepository
) {
    fun register(request: RegisterRequest): AuthenticationResponse {
        if (userRepository.existsByEmail(request.email)) {
            return AuthenticationResponse(msg = "Already exist by email")
        }
        if (userRepository.existsByPhoneNumber(request.phoneNumber)) {
            return AuthenticationResponse(msg = "Already exist by phone number")
        }

        val user = User(
            firstName = request.firstname,
            lastName = request.lastname,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            phoneNumber = request.phoneNumber,
            role = request.role
        )
        val savedUser = userRepository.save(user)
        val jwtToken = jwtService.generateToken(user)
        val refreshToken = jwtService.generateRefreshToken(user)
        saveToken(savedUser, jwtToken)

        return AuthenticationResponse(
            accessToken = jwtToken,
            refreshToken = refreshToken
        )
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        val user = userRepository.findByEmail(request.email).orElseThrow()
        val token = jwtService.generateToken(user)
        revokeTokens(user)
        val refreshToken = jwtService.generateRefreshToken(user)
        saveToken(user, token)

        return AuthenticationResponse(
            accessToken = token,
            refreshToken = refreshToken
        )
    }

    private fun saveToken(user: User, jwtToken: String) {
        val token = Token(
            user = user,
            token = jwtToken,
            expired = false,
            tokenType = TokenType.BEARER,
            revoked = false
        )
        tokenRepository.save(token)
    }

    private fun revokeTokens(user: User) {
        val validUserTokens = tokenRepository.findAllValidTokensByUser(user.id)
        if (validUserTokens.isEmpty()) return

        val updatedTokens = validUserTokens.map { token ->
            token.copy(expired = true, revoked = true)  // Yangi nusxa yaratish
        }

        tokenRepository.saveAll(updatedTokens)
    }

    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        val authHeader = request.getHeader("Authorization") ?: return
        if (!authHeader.startsWith("Bearer ")) return

        val refreshToken = authHeader.substring(7)
        val userEmail = jwtService.extractUsername(refreshToken)?.lowercase()

        if (userEmail != null) {
            userRepository.findByEmail(userEmail).ifPresent { user ->
                if (jwtService.validateToken(refreshToken,user)) {
                    val accessToken = jwtService.generateToken(user)
                    revokeTokens(user)
                    saveToken(user, accessToken)
                    val authResponse = AuthenticationResponse(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                    ObjectMapper().writeValue(response.outputStream, authResponse)
                }
            }
        }
    }

    fun getAllUsers(): List<User> = userRepository.findAll()

    fun validateToken(token: String): Boolean = jwtService.isValidateToken(token)
}