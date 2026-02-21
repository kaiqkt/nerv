package com.kaiqkt.nerv.domain.services

import com.kaiqkt.nerv.domain.exceptions.DomainException
import com.kaiqkt.nerv.domain.exceptions.ErrorType
import com.kaiqkt.nerv.domain.models.User
import com.kaiqkt.nerv.domain.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    private val log = LoggerFactory.getLogger(UserService::class.java)

    fun create(user: User) {
        if (userRepository.existsByEmail(user.email)) {
            throw DomainException(ErrorType.EMAIL_ALREADY_EXISTS)
        }

        if (userRepository.existsByNickname(user.nickname)) {
            throw DomainException(ErrorType.NICKNAME_ALREADY_EXISTS)
        }

        user.password = passwordEncoder.encode(user.password)

        userRepository.save(user)

        log.info("Created user ${user.id} successfully")
    }

    fun findById(id: String): User = userRepository.findById(id).getOrNull() ?: throw DomainException(ErrorType.USER_NOT_FOUND)
}
