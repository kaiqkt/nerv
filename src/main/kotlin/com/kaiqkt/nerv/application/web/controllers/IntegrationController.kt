package com.kaiqkt.nerv.application.web.controllers

import com.kaiqkt.nerv.application.security.SecurityContext
import com.kaiqkt.nerv.application.web.responses.GitAccessTokenResponse
import com.kaiqkt.nerv.application.web.responses.GitAuthResponse
import com.kaiqkt.nerv.application.web.responses.toResponse
import com.kaiqkt.nerv.domain.results.GitAccessTokenResult
import com.kaiqkt.nerv.domain.services.GitService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/integrations")
class IntegrationController(
    private val gitService: GitService,
) {
    @PostMapping("/github/device")
    fun getDeviceCode(): ResponseEntity<GitAuthResponse> {
        val dto = gitService.getDeviceCode()

        return ResponseEntity.ok(dto.toResponse())
    }

    @PostMapping("/github/token")
    fun accessToken(
        @RequestParam code: String,
    ): ResponseEntity<GitAccessTokenResponse> {
        val userId = SecurityContext.getUserId()
        return when (val response = gitService.getAccessToken(code, userId)) {
            is GitAccessTokenResult.Success -> ResponseEntity.noContent().build()
            is GitAccessTokenResult.Failure -> ResponseEntity.badRequest().body(GitAccessTokenResponse(response.error))
        }
    }
}
