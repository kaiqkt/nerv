package com.kaiqkt.nerv.application.security

import com.kaiqkt.nerv.application.exceptions.UnauthorizedException
import org.springframework.security.core.context.SecurityContextHolder

object SecurityContext {
    private const val INVALID_TOKEN_ERROR = "INVALID_TOKEN"

    fun getUserId(): String {
        val context = SecurityContextHolder.getContext()
        val authentication = context.authentication as NervAuthentication

        if (authentication.userId != null) {
            return authentication.userId
        }

        throw UnauthorizedException("User id not available", INVALID_TOKEN_ERROR)
    }
}
