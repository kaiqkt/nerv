package com.kaiqkt.nerv.application.security

import org.springframework.security.core.context.SecurityContextHolder

object SecurityContext {
    fun getUserId(): String {
        val context = SecurityContextHolder.getContext()
        val authentication = context.authentication as NervAuthentication

        return authentication.principal as String
    }
}
