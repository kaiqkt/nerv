package com.kaiqkt.nerv.application.web.responses

import com.kaiqkt.nerv.domain.dtos.GitOAuthDto

data class GitOAuthResponse(
    val userCode: String,
    val verificationUri: String
)

fun GitOAuthDto.toResponse(): GitOAuthResponse {
    return GitOAuthResponse(
        userCode = this.userCode,
        verificationUri = this.verificationUri
    )
}