package com.kaiqkt.nerv.application.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class NervAuthentication(
    val userId: String?,
) : Authentication {
    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        TODO("Not yet implemented")
    }

    override fun getCredentials(): Any? {
        TODO("Not yet implemented")
    }

    override fun getDetails(): Any? {
        TODO("Not yet implemented")
    }

    override fun getPrincipal(): Any? {
        TODO("Not yet implemented")
    }

    override fun isAuthenticated(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getName(): String? {
        TODO("Not yet implemented")
    }
}
