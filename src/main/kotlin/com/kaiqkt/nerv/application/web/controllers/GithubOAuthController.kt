package com.kaiqkt.nerv.application.web.controllers

import com.kaiqkt.nerv.application.web.responses.GitOAuthResponse
import com.kaiqkt.nerv.application.web.responses.toResponse
import com.kaiqkt.nerv.domain.services.GitOAuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/github/oauth")
class GithubOAuthController(
    private val gitOAuthService: GitOAuthService
) {

    @PostMapping("/device")
    fun requestDeviceCode(): ResponseEntity<GitOAuthResponse> {
        val deviceCode = gitOAuthService.requestDeviceCode()

        return ResponseEntity.ok(deviceCode.toResponse())
    }

//    @PostMapping("/poll")
//    fun poll(@RequestParam deviceCode: String): TokenResponse =
//        githubOAuthClient.pollForToken(deviceCode)
}