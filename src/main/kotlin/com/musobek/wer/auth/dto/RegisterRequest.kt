package com.musobek.wer.auth.dto

import com.musobek.wer.auth.model.enam.Role

data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val role: Role
)