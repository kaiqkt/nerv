package com.kaiqkt.nerv.domain.repositories

import com.kaiqkt.nerv.domain.models.GitDeviceAuth
import com.kaiqkt.nerv.domain.models.enums.GitDeviceAuthStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface GitDeviceAuthRepository: JpaRepository<GitDeviceAuth, String> {
    fun findAllByStatusAndPollAtIsBefore(status: GitDeviceAuthStatus, now: LocalDateTime): List<GitDeviceAuth>
}