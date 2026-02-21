package com.kaiqkt.nerv.domain.gateways

import com.kaiqkt.nerv.domain.results.GitAccessTokenResult

interface GitGateway {
    fun getAccessToken(code: String): GitAccessTokenResult

    fun getAuthorizeUrl(): String
}
