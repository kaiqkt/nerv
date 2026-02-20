package com.kaiqkt.nerv.domain.repositories

import com.kaiqkt.nerv.domain.models.Lock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LockRepository: JpaRepository<Lock, Long> {
    fun deleteByName(name: String)
    fun existsByName(name: String): Boolean
}