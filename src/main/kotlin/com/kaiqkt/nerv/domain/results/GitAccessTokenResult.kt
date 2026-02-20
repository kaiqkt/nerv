package com.kaiqkt.nerv.domain.results

sealed class GitAccessTokenResult {
    data class Success(val accessToken: String) : GitAccessTokenResult()
    object Denied : GitAccessTokenResult()
    object Pending : GitAccessTokenResult()
    data class Slowdown(val interval: Int): GitAccessTokenResult()
    object Expired: GitAccessTokenResult()
    data class Failure(val message: String?): GitAccessTokenResult()
}