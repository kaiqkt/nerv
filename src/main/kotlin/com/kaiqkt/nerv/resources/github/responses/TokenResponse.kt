package com.kaiqkt.nerv.resources.github.responses

data class TokenResponse(
    val accessToken: String?,
    val error: String?,
    val interval: Int?
)