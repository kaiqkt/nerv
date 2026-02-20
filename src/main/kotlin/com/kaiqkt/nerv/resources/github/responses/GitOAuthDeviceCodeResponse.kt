package com.kaiqkt.nerv.resources.github.responses

data class GitOAuthDeviceCodeResponse(
    val deviceCode: String,
    val userCode: String,
    val verificationUri: String,
    val interval: Int,
    val expiresIn: Int
)

