package com.musobek.wer.auth.repo

import com.musobek.wer.auth.model.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TokenRepository : JpaRepository<Token, Int> {

    @Query("""
        SELECT t FROM Token t INNER JOIN t.user u 
        WHERE u.id = :userId AND (t.expired = false OR t.revoked = false)
    """)
    fun findAllValidTokensByUser(@Param("userId") userId: Long?): List<Token>

    fun findByToken(token: String): Optional<Token>
}