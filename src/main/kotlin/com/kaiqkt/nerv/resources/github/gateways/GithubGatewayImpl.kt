package com.kaiqkt.nerv.resources.github.gateways

import com.kaiqkt.nerv.domain.gateways.GitGateway
import com.kaiqkt.nerv.domain.results.GitAccessTokenResult
import com.kaiqkt.nerv.resources.github.GithubOAuthProperties
import com.kaiqkt.nerv.resources.github.clients.GithubAuthClient
import org.springframework.stereotype.Component

@Component
class GithubGatewayImpl(
    private val githubAuthClient: GithubAuthClient,
    private val properties: GithubOAuthProperties,
) : GitGateway {
    override fun getAuthorizeUrl(): String =
        "${properties.url}/login/oauth/authorize?" +
            "client_id=${properties.clientId}" +
            "&redirect_uri=${properties.redirectUri}&scope=${TOKEN_SCOPES}"

    override fun getAccessToken(code: String): GitAccessTokenResult {
        val response = githubAuthClient.accessToken(code)

        if (response.accessToken != null) {
            return GitAccessTokenResult.Success(response.accessToken)
        }

        return GitAccessTokenResult.Failure(response.error ?: "Unknown error")
    }

    companion object {
        private const val TOKEN_SCOPES = "repo"
    }
}
