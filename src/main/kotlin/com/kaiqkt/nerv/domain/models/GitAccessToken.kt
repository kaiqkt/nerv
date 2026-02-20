package com.kaiqkt.nerv.domain.models

import io.azam.ulidj.ULID
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "git_access_token")
@EntityListeners(AuditingEntityListener::class)
class GitAccessToken(
    val accessToken: String = "",
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    val user: User = User(),
) {
    @Id
    val id: String = ULID.random()

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()
}