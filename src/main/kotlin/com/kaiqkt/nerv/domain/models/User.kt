package com.kaiqkt.nerv.domain.models

import io.azam.ulidj.ULID
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
class User(
    val name: String = "",
    val nickname: String = "",
    val email: String = "",
    var password: String = "",
) {
    @Id
    val id: String = ULID.random()

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()
}
