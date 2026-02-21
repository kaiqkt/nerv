package com.kaiqkt.nerv.application.web.requests

import com.kaiqkt.nerv.domain.dtos.ProjectDto
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

sealed class ProjectRequest {
    data class Create(
        @field:NotBlank(message = "input should not be blank")
        @field:Size(message = "must not exceed 25 characters", max = 25)
        val name: String,
        @field:Size(message = "must not exceed 255 characters", max = 255)
        val description: String?,
    ) : ProjectRequest()
}

fun ProjectRequest.Create.toDomain(): ProjectDto.Create =
    ProjectDto.Create(
        name = this.name,
        description = this.description,
    )
