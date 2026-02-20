package com.kaiqkt.nerv.domain.models

import io.azam.ulidj.ULID
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "locks")
class Lock(
    @Id
    val name: String = ULID.random(),
    val lockedUntil: LocalDateTime = LocalDateTime.now(),
)