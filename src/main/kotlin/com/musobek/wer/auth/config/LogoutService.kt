package com.musobek.wer.auth.config

import com.musobek.wer.auth.repo.TokenRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service

@Service
class LogoutService(private val tokenRepository: TokenRepository) : LogoutHandler {

    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication?) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return
        }

        val jwt = authHeader.substring(7)
        val storedToken = tokenRepository.findByToken(jwt).orElse(null)

        storedToken?.let {
            val copyToken = it.copy(expired = true, revoked = true)
            tokenRepository.save(copyToken)
        }
    }
}