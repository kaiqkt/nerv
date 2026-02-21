package com.kaiqkt.nerv.domain.services

import com.kaiqkt.nerv.domain.dtos.ProjectDto
import com.kaiqkt.nerv.domain.exceptions.DomainException
import com.kaiqkt.nerv.domain.exceptions.ErrorType
import com.kaiqkt.nerv.domain.models.Project
import com.kaiqkt.nerv.domain.repositories.ProjectRepository
import com.kaiqkt.nerv.utils.Constants.Metrics.CREATED
import com.kaiqkt.nerv.utils.Constants.Metrics.STATUS
import com.kaiqkt.nerv.utils.MetricsUtils
import org.springframework.stereotype.Service

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val userService: UserService,
    private val metricsUtils: MetricsUtils,
) {
    fun create(
        project: ProjectDto.Create,
        userId: String,
    ): Project {
        val user = userService.findById(userId)

        val project =
            Project(
                name = project.name,
                description = project.description,
                user = user,
            )

        if (projectRepository.existsBySlug(project.slug)) {
            throw DomainException(ErrorType.PROJECT_ALREADY_EXISTS)
        }

        return projectRepository.save(project).also {
            metricsUtils.counter("project", STATUS, CREATED)
        }
    }
}
