package com.musobek.wer.auth.dto

data class AuthenticationRequest(
    val email: String,
    val password: String
)