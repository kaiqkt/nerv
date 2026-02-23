package com.kaiqkt.nerv.resources.github.responses

data class GithubDeviceCodeResponse(
    val deviceCode: String,
    val userCode: String,
    val verificationUri: String,
    val expiresIn: Int,
    val interval: Int
)
