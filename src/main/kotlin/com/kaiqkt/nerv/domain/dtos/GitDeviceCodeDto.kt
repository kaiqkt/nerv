package com.kaiqkt.nerv.domain.dtos

data class GitDeviceCodeDto(
    val deviceCode: String,
    val userCode: String,
    val verificationUri: String,
    val expiresIn: Int,
    val interval: Int
)
