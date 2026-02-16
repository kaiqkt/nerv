package com.kaiqkt.nerv.domain.models

import com.github.slugify.Slugify
import io.azam.ulidj.ULID
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "projects")
@EntityListeners(AuditingEntityListener::class)
class Project(
    val name: String = "",
    val description: String? = null,
) {
    @Id
    val id: String = ULID.random()
    val slug: String = Slugify.builder().build().slugify(name)

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()
}
