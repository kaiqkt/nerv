package com.kaiqkt.nerv.domain.dtos

sealed class ProjectDto {
    data class Create(
        val name: String,
        val description: String?,
    ) : ProjectDto()
}
