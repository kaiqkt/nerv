package com.kaiqkt.nerv.resources.github.gateways

import com.kaiqkt.nerv.domain.dtos.GitDeviceCodeDto
import com.kaiqkt.nerv.domain.gateways.GitGateway
import com.kaiqkt.nerv.domain.results.GitAccessTokenResult
import com.kaiqkt.nerv.resources.github.clients.GithubClient
import org.springframework.stereotype.Component

@Component
class GithubGatewayImpl(
    private val githubClient: GithubClient
) : GitGateway {
    override fun getDeviceCode(): GitDeviceCodeDto {
        val response = githubClient.requestDeviceCode()

        return GitDeviceCodeDto(
            deviceCode = response.deviceCode,
            userCode = response.userCode,
            verificationUri = response.verificationUri,
            expiresIn = response.expiresIn,
            interval = response.interval
        )
    }

    override fun getAccessToken(code: String): GitAccessTokenResult {
        val expectedErrors =
            listOf("authorization_pending", "slow_down", "expired_token", "incorrect_device_code", "access_denied")
        val response = githubClient.accessToken(code)

        if (response.accessToken != null) {
            return GitAccessTokenResult.Success(response.accessToken)
        }

        val error = expectedErrors.find { it == response.error } ?: "UNEXPECTED_ERROR"

        return GitAccessTokenResult.Failure(error)
    }

    companion object {
        private const val TOKEN_SCOPES = "repo"
    }
}
