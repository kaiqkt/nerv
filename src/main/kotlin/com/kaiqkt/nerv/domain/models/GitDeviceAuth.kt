package com.kaiqkt.nerv.domain.models

import com.kaiqkt.nerv.domain.models.enums.GitDeviceAuthStatus
import io.azam.ulidj.ULID
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "git_device_auth")
@EntityListeners(AuditingEntityListener::class)
class GitDeviceAuth(
    val deviceCode: String = "",
    var pollAt: LocalDateTime = LocalDateTime.now(),
    val expiresAt: LocalDateTime = LocalDateTime.now(),
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    val user: User = User(),
) {
    @Id
    val id: String = ULID.random()

    @Enumerated(EnumType.STRING)
    var status = GitDeviceAuthStatus.PENDING
    var lastPostedAt: LocalDateTime = LocalDateTime.now()

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()
}