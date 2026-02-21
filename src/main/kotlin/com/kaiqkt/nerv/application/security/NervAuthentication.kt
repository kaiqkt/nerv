package com.kaiqkt.nerv.application.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class NervAuthentication(
    private val userId: String,
    private val roles: List<String>,
    private val token: String
) : Authentication {

    private var authenticated: Boolean = true

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it) }
    }

    override fun getCredentials(): Any {
        return token
    }

    override fun getDetails(): Any {
        return mapOf(
            "user_id" to userId,
        )
    }

    override fun getPrincipal(): Any {
        return userId
    }

    override fun isAuthenticated(): Boolean {
        return authenticated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }

    override fun getName(): String {
        return userId
    }
}
