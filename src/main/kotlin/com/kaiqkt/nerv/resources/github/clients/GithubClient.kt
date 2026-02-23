package com.kaiqkt.nerv.resources.github.clients

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.isSuccessful
import com.kaiqkt.nerv.resources.exceptions.UnexpectedResourceException
import com.kaiqkt.nerv.resources.github.GithubOAuthProperties
import com.kaiqkt.nerv.resources.github.responses.GithubAccessTokenResponse
import com.kaiqkt.nerv.resources.github.responses.GithubDeviceCodeResponse
import com.kaiqkt.nerv.utils.MetricsUtils
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class GithubClient(
    private val metricsUtils: MetricsUtils,
    private val mapper: ObjectMapper,
    private val properties: GithubOAuthProperties,
) {
    fun requestDeviceCode(): GithubDeviceCodeResponse {
        val (_, response, result) =
            metricsUtils.request("github_oauth_device_token") {
                Fuel
                    .post("${properties.url}/login/device/code")
                    .header(
                        mapOf(
                            "Content-Type" to MediaType.APPLICATION_FORM_URLENCODED,
                            "Accept" to MediaType.APPLICATION_JSON
                        )
                    )
                    .body("client_id=${properties.clientId}&scope=$DEVICE_TOKEN_SCOPE")
                    .response()
            }

        if (response.isSuccessful) {
            return mapper.readValue(result.get(), GithubDeviceCodeResponse::class.java)
        }

        throw UnexpectedResourceException("Fail to get github device token ${response.responseMessage}")
    }

    fun accessToken(deviceCode: String): GithubAccessTokenResponse {
        val (_, response, result) =
            metricsUtils.request("github_oauth_access_token") {
                Fuel
                    .post("${properties.url}/login/oauth/access_token")
                    .header(
                        mapOf(
                            "Content-Type" to MediaType.APPLICATION_FORM_URLENCODED,
                            "Accept" to MediaType.APPLICATION_JSON
                        )
                    )
                    .body(
                        "client_id=${properties.clientId}" +
                                "&client_secret=${properties.clientSecret}" +
                                "&device_code=$deviceCode" +
                                "&grant_type=urn:ietf:params:oauth:grant-type:device_code"
                    )
                    .response()
            }

        if (response.isSuccessful) {
            return mapper.readValue(result.get(), GithubAccessTokenResponse::class.java)
        }

        throw UnexpectedResourceException("Fail to request github access token ${response.responseMessage}")
    }

    companion object {
        private const val DEVICE_TOKEN_SCOPE = "repo"
    }
}
