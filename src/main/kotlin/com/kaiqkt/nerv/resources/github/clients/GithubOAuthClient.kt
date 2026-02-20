package com.kaiqkt.nerv.resources.github.clients

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.isSuccessful
import com.kaiqkt.nerv.resources.exceptions.UnexpectedResourceException
import com.kaiqkt.nerv.resources.github.GithubOAuthProperties
import com.kaiqkt.nerv.resources.github.responses.GitOAuthDeviceCodeResponse
import com.kaiqkt.nerv.resources.github.responses.TokenResponse
import com.kaiqkt.nerv.utils.MetricsUtils
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class GithubOAuthClient(
    private val metricsUtils: MetricsUtils,
    private val mapper: ObjectMapper,
    private val properties: GithubOAuthProperties
) {

    fun requestDeviceToken(): GitOAuthDeviceCodeResponse {
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
            return mapper.readValue(result.get(), GitOAuthDeviceCodeResponse::class.java)
        }

        throw UnexpectedResourceException("Fail to get device token ${response.responseMessage}")
    }

    fun pollForToken(deviceCode: String): TokenResponse {
        val (_, response, result) =
            metricsUtils.request("github_oauth_poll_token") {
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
            return mapper.readValue(result.get(), TokenResponse::class.java)
        }

        throw UnexpectedResourceException("Fail to poll token ${response.responseMessage}")
    }

    companion object {
        private const val DEVICE_TOKEN_SCOPE = "repo"
    }
}