package com.kaiqkt.nerv.domain.services

import com.kaiqkt.nerv.domain.dtos.GitDeviceCodeDto
import com.kaiqkt.nerv.domain.exceptions.DomainException
import com.kaiqkt.nerv.domain.exceptions.ErrorType
import com.kaiqkt.nerv.domain.gateways.GitGateway
import com.kaiqkt.nerv.domain.models.GitAccessToken
import com.kaiqkt.nerv.domain.models.User
import com.kaiqkt.nerv.domain.repositories.GitAccessTokenRepository
import com.kaiqkt.nerv.domain.repositories.UserRepository
import com.kaiqkt.nerv.domain.results.GitAccessTokenResult
import org.springframework.stereotype.Service

@Service
class GitService(
    private val gitGateway: GitGateway,
    private val cryptoService: CryptoService,
    private val gitAccessTokenRepository: GitAccessTokenRepository,
    private val userService: UserService,
) {


    fun getDeviceCode(): GitDeviceCodeDto = gitGateway.getDeviceCode()

    fun getAccessToken(
        code: String,
        userId: String,
    ): GitAccessTokenResult {
        if (gitAccessTokenRepository.existsByUserId(userId)) {
            throw DomainException(ErrorType.GIT_ACCESS_TOKEN_ALREADY_EXISTS)
        }

        val user = userService.findById(userId)
        val response = gitGateway.getAccessToken(code)

        if (response is GitAccessTokenResult.Success) {
            val accessToken =
                GitAccessToken(
                    user = user,
                    accessToken = cryptoService.encrypt(response.accessToken),
                )

            gitAccessTokenRepository.save(accessToken)
        }

        return response
    }
}
