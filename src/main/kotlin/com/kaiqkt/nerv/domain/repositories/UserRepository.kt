package com.kaiqkt.nerv.domain.repositories

import com.kaiqkt.nerv.domain.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun existsByEmail(email: String): Boolean

    fun existsByNickname(nickname: String): Boolean
}
