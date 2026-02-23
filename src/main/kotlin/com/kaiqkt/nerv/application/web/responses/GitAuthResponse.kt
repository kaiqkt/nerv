package com.kaiqkt.nerv.application.web.responses

import com.kaiqkt.nerv.domain.dtos.GitDeviceCodeDto

data class GitAuthResponse(
    val deviceCode: String,
    val userCode: String,
    val verificationUri: String,
    val expiresIn: Int,
    val interval: Int
)

fun GitDeviceCodeDto.toResponse() = GitAuthResponse(
    deviceCode = this.deviceCode,
    userCode = this.userCode,
    verificationUri = this.verificationUri,
    expiresIn = this.expiresIn,
    interval = this.interval
)
