package com.kaiqkt.nerv.application.security

import com.kaiqkt.nerv.domain.services.TokenService
import com.kaiqkt.nerv.utils.Constants
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationFilter(
    private val tokenService: TokenService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (!authorizationHeader.isNullOrBlank() &&
            authorizationHeader.startsWith(BEARER, ignoreCase = true)
        ) {
            val token = authorizationHeader.substringAfter(BEARER).trim()

            val authentication = handleAccessToken(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)

    }

    private fun handleAccessToken(token: String): NervAuthentication {
        val claims = tokenService.getClaims(token)
        val userId = claims.subject
        val roles = claims.getStringListClaim(Constants.Keys.ROLES)

        return NervAuthentication(
            userId = userId,
            roles = roles,
            token = token
        )
    }

    companion object {
        private const val BEARER = "Bearer "
    }

}