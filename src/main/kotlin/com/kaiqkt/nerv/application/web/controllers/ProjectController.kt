package com.kaiqkt.nerv.application.web.controllers

import com.kaiqkt.nerv.application.web.requests.ProjectRequest
import com.kaiqkt.nerv.application.web.requests.toDomain
import com.kaiqkt.nerv.application.web.responses.ProjectResponse
import com.kaiqkt.nerv.application.web.responses.toResponse
import com.kaiqkt.nerv.domain.services.ProjectService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/projects")
class ProjectController(
    private val projectService: ProjectService
) {

    @PostMapping
    fun create(
        @Valid @RequestBody request: ProjectRequest.Create,
    ): ResponseEntity<ProjectResponse> {
        val project = projectService.create(request.toDomain())

        return ResponseEntity.ok(project.toResponse())
    }

}