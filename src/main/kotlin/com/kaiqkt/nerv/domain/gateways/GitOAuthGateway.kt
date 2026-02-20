package com.kaiqkt.nerv.domain.gateways

import com.kaiqkt.nerv.domain.dtos.GitOAuthDto
import com.kaiqkt.nerv.domain.results.GitAccessTokenResult

interface GitOAuthGateway {
    fun requestDeviceCode(): GitOAuthDto
    fun pollAccessToken(deviceCode: String): GitAccessTokenResult
}