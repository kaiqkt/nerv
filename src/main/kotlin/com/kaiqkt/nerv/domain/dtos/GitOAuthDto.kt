package com.kaiqkt.nerv.domain.dtos

import java.time.LocalDateTime

data class GitOAuthDto(
    val deviceCode: String,
    val userCode: String,
    val verificationUri: String,
    val interval: Int,
    val expiresAt: LocalDateTime
)
