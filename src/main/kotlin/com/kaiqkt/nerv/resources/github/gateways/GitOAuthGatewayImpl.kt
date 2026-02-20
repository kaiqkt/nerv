package com.kaiqkt.nerv.resources.github.gateways

import com.kaiqkt.nerv.domain.dtos.GitOAuthDto
import com.kaiqkt.nerv.domain.gateways.GitOAuthGateway
import com.kaiqkt.nerv.domain.results.GitAccessTokenResult
import com.kaiqkt.nerv.resources.github.clients.GithubOAuthClient
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class GitOAuthGatewayImpl(
    private val githubOAuthClient: GithubOAuthClient
) : GitOAuthGateway {
    override fun requestDeviceCode(): GitOAuthDto {
        val response = githubOAuthClient.requestDeviceToken()

        return GitOAuthDto(
            deviceCode = response.deviceCode,
            userCode = response.userCode,
            verificationUri = response.verificationUri,
            interval = response.interval,
            expiresAt = LocalDateTime.now().plusSeconds(response.expiresIn.toLong())
        )
    }

    override fun pollAccessToken(deviceCode: String): GitAccessTokenResult {
        val response = githubOAuthClient.pollForToken(deviceCode)

        if (response.accessToken != null) {
            return GitAccessTokenResult.Success(response.accessToken)
        }

        return handleErrorResponse(response.error, response.interval)
    }

    private fun handleErrorResponse(error: String?, interval: Int?): GitAccessTokenResult {
        if (error != null && error == "slow_down" && interval != null) {
            return GitAccessTokenResult.Slowdown(interval)
        }

        return when (error) {
            "access_denied", "incorrect_device_code" -> GitAccessTokenResult.Denied
            "device_flow_disabled", "incorrect_client_credentials" -> GitAccessTokenResult.Denied
            "authorization_pending" -> GitAccessTokenResult.Pending
            "expired_token" -> GitAccessTokenResult.Expired
            else -> GitAccessTokenResult.Failure(error)
        }
    }
}