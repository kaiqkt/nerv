package com.kaiqkt.nerv.domain.services

import com.kaiqkt.nerv.domain.models.Lock
import com.kaiqkt.nerv.domain.repositories.LockRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LockService(
    private val lockRepository: LockRepository,
    @param:Value($$"${lock-interval}")
    private val lockInterval: Long
) {
    fun lock(name: String) {
        val lock = Lock(
            name = name,
            lockedUntil = LocalDateTime.now().plusSeconds(lockInterval),
        )

        lockRepository.save(lock)
    }

    fun isLocked(name: String): Boolean {
        return lockRepository.existsByName(name)
    }

    fun unlock(name: String) {
        lockRepository.deleteByName(name)
    }
}