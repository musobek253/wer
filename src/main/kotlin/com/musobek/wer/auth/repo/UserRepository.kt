package com.musobek.wer.auth.repo


import com.musobek.wer.auth.model.User
import org.springframework.data.jpa.repository.JpaRepository

import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): Optional<User>
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phone: String): Boolean
//    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber")
//    fun existsByPhoneNumber(@Param("phoneNumber") phoneNumber: String): Boolean
}