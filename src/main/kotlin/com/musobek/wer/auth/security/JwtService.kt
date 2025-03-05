package com.musobek.wer.auth.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JWTService {
    private val secretKey = "MusobekSecretKeyMusobekSecretKeyMusobekSecretKey"
    private val jwtExpiration: Long = 1000 * 60 * 60 * 10
    private  val  refreshExpiration: Long = 1000 * 60// 32 ta belgidan iborat kalit

    fun generateTokenWithClaims(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        return buildJwtToken(extraClaims, userDetails, jwtExpiration)
    }
    // Tokenni yangilash (refresh)
    fun generateRefreshToken(userDetails: UserDetails): String {
        return buildJwtToken(emptyMap(), userDetails, refreshExpiration)
    }
    fun generateToken(userDetails: UserDetails): String {
        return generateTokenWithClaims(emptyMap(), userDetails)
    }

    private fun buildJwtToken(extraClaims: Map<String, Any>, userDetails: UserDetails, expiration: Long): String {
        return Jwts.builder()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + jwtExpiration)) // 10 soat
            .signWith(key)
            .compact()
    }

    private val key: SecretKey
        get() {
            val keyBytes = Decoders.BASE64.decode(secretKey)
            return Keys.hmacShaKeyFor(keyBytes)
        }

    fun extractUsername(token: String): String? {
        return extractClaim(token) { it.subject }
    }

    private fun <T> extractClaim(token: String, claimResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val userName = extractUsername(token)
        return (userName == userDetails.username && !isTokenExpired(token))
    }
    fun isValidateToken(token: String): Boolean {
        return try {
            !isTokenExpired(token)
        } catch (e: Exception) {
            false
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }
}