package com.kaiqkt.nerv.domain.repositories

import com.kaiqkt.nerv.domain.models.GitAccessToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GitAccessTokenRepository : JpaRepository<GitAccessToken, Long> {
    fun existsByUserId(userId: String): Boolean
}
