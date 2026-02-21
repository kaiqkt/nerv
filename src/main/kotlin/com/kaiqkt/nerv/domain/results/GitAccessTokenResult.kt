package com.kaiqkt.nerv.domain.results

sealed class GitAccessTokenResult {
    data class Success(
        val accessToken: String,
    ) : GitAccessTokenResult()

    data class Failure(
        val message: String,
    ) : GitAccessTokenResult()
}
