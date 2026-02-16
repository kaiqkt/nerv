package com.kaiqkt.nerv.application.web.responses

import com.kaiqkt.nerv.domain.models.Project
import java.time.LocalDateTime

data class ProjectResponse(
    val id: String,
    val name: String,
    val slug: String,
    val description: String?,
    val createdAt: LocalDateTime
)

fun Project.toResponse(): ProjectResponse {
    return ProjectResponse(
        id = this.id,
        name = this.name,
        slug = this.slug,
        description = this.description,
        createdAt = this.createdAt
    )
}