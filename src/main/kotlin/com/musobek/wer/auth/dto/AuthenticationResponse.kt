package com.musobek.wer.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
class AuthenticationResponse {
    @JsonProperty("access_token")
    private var accessToken: String? = null

    @JsonProperty("refresh_token")
    private var refreshToken: String? = null

    private var msg: String? = null

    constructor(accessToken: String?, refreshToken: String?) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    constructor(msg: String?) {
        this.msg = msg
    }
}