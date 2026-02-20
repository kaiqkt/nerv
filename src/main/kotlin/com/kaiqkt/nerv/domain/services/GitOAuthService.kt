package com.kaiqkt.nerv.domain.services

import com.kaiqkt.nerv.domain.dtos.GitOAuthDto
import com.kaiqkt.nerv.domain.gateways.GitOAuthGateway
import com.kaiqkt.nerv.domain.models.GitAccessToken
import com.kaiqkt.nerv.domain.models.GitDeviceAuth
import com.kaiqkt.nerv.domain.models.enums.GitDeviceAuthStatus
import com.kaiqkt.nerv.domain.repositories.GitAccessTokenRepository
import com.kaiqkt.nerv.domain.repositories.GitDeviceAuthRepository
import com.kaiqkt.nerv.domain.results.GitAccessTokenResult
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GitOAuthService(
    private val gitOAuthGateway: GitOAuthGateway,
    private val cryptoService: CryptoService,
    private val gitDeviceAuthRepository: GitDeviceAuthRepository,
    private val gitAccessTokenRepository: GitAccessTokenRepository,
    private val lockService: LockService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val LOCK_NAME = "git_poll_token_lock"
    }

    //validar se ja nao existe um access token associado por usuario
    //se ja existir uma request de device auth, se existir cancelar
    fun requestDeviceCode(): GitOAuthDto {
        val deviceCodeDto = gitOAuthGateway.requestDeviceCode()
        val deviceCode = cryptoService.encrypt(deviceCodeDto.deviceCode)
        val deviceAuth = GitDeviceAuth(
            deviceCode = deviceCode,
            pollAt = LocalDateTime.now().plusSeconds(deviceCodeDto.interval.toLong()),
            expiresAt = deviceCodeDto.expiresAt
        )

        gitDeviceAuthRepository.save(deviceAuth)

        return deviceCodeDto
    }

    //endpoint para retornar login do access token
    //caso retorne 401 deletar o access token do banco de daos

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    private fun saveAccessToken(accessToken: String) {
        val encryptedAccessToken = cryptoService.encrypt(accessToken)
        gitAccessTokenRepository.save(GitAccessToken(encryptedAccessToken))

        //metrica
    }

    @Scheduled(fixedDelay = 5_000)
    @Transactional
    fun triggerPoll() {
        if (lockService.isLocked(LOCK_NAME)) {
            return
        }

        try {
            lockService.lock(LOCK_NAME)
            val now = LocalDateTime.now()
            val devicesAuth = gitDeviceAuthRepository.findAllByStatusAndPollAtIsBefore(GitDeviceAuthStatus.PENDING, now)

            devicesAuth.forEach { pollOAuthToken(it, now) }
        } finally {
            lockService.unlock(LOCK_NAME)
        }
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    private fun pollOAuthToken(deviceAuth: GitDeviceAuth, now: LocalDateTime) {
        if (now.isAfter(deviceAuth.expiresAt)) {
            deviceAuth.status = GitDeviceAuthStatus.EXPIRED
            return
        }

        if (now.isBefore(deviceAuth.pollAt)) {
            return
        }

        val deviceCode = cryptoService.decrypt(deviceAuth.deviceCode)

        when (val response = gitOAuthGateway.pollAccessToken(deviceCode)) {
            is GitAccessTokenResult.Denied -> deviceAuth.status = GitDeviceAuthStatus.DENIED
            is GitAccessTokenResult.Success -> {
                deviceAuth.status = GitDeviceAuthStatus.APPROVED
                saveAccessToken(response.accessToken)
            }
            is GitAccessTokenResult.Expired -> deviceAuth.status = GitDeviceAuthStatus.EXPIRED
            is GitAccessTokenResult.Slowdown -> deviceAuth.pollAt = now.plusSeconds(response.interval.toLong())
            is GitAccessTokenResult.Pending -> deviceAuth.lastPostedAt = now
            is GitAccessTokenResult.Failure -> {
                deviceAuth.lastPostedAt = now
                log.info("Unexpected failure ${response.message}")
            }
        }
    }
}