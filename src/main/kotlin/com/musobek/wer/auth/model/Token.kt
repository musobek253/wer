package com.musobek.wer.auth.model

import com.musobek.wer.auth.model.enam.TokenType

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data


@Entity
@Table(name = "tokens") // Jadval nomini qo'shdik
data class Token(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,  // Nullable qilish yaxshiroq
    val token: String,

    @Enumerated(EnumType.STRING)
    val tokenType: TokenType,

    val expired: Boolean = false, // Default qiymat false

    val revoked: Boolean = false, // Default qiymat false

    @ManyToOne
    @JoinColumn(name = "user_id") // Xatolikni to'g'irladik (user-id emas, user_id bo'lishi kerak)
    val user: User
)