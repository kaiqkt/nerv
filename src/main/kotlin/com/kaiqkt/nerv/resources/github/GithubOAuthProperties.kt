package com.kaiqkt.nerv.resources.github

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "github.oauth")
data class GithubOAuthProperties(
    val url: String,
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
)
