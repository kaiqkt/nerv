package com.kaiqkt.nerv.unit.domain.services

import com.kaiqkt.nerv.domain.dtos.ProjectDto
import com.kaiqkt.nerv.domain.exceptions.DomainException
import com.kaiqkt.nerv.domain.exceptions.ErrorType
import com.kaiqkt.nerv.domain.models.Project
import com.kaiqkt.nerv.domain.models.User
import com.kaiqkt.nerv.domain.repositories.ProjectRepository
import com.kaiqkt.nerv.domain.services.ProjectService
import com.kaiqkt.nerv.domain.services.UserService
import com.kaiqkt.nerv.utils.Constants.Metrics.CREATED
import com.kaiqkt.nerv.utils.Constants.Metrics.STATUS
import com.kaiqkt.nerv.utils.MetricsUtils
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ProjectServiceTest {
    private val projectRepository: ProjectRepository = mockk()
    private val userService: UserService = mockk()
    private val metricsUtils: MetricsUtils = mockk()
    private val projectService = ProjectService(projectRepository, userService, metricsUtils)

    @Test
    fun `given a valid request when creating a project then return the created project`() {
        val userId = "user-1"
        val user = User(name = "User 1", nickname = "user1", email = "user1@mail.com")
        val request = ProjectDto.Create(name = "Project 1", description = "Description 1")
        val projectSlot = slot<Project>()

        every { userService.findById(userId) } returns user
        every { projectRepository.existsBySlug(any()) } returns false
        every { projectRepository.save(capture(projectSlot)) } answers { projectSlot.captured }
        every { metricsUtils.counter("project", STATUS, CREATED) } returns Unit

        val result = projectService.create(request, userId)

        assertEquals(request.name, result.name)
        assertEquals(request.description, result.description)
        verify(exactly = 1) { userService.findById(userId) }
        verify(exactly = 1) { projectRepository.existsBySlug(result.slug) }
        verify(exactly = 1) { projectRepository.save(any()) }
        verify(exactly = 1) { metricsUtils.counter("project", STATUS, CREATED) }
    }

    @Test
    fun `given a project already exists with same slug when creating then throw DomainException`() {
        val userId = "user-1"
        val user = User(name = "User 1", nickname = "user1", email = "user1@mail.com")
        val request = ProjectDto.Create(name = "Project 1", description = "Description 1")

        every { userService.findById(userId) } returns user
        every { projectRepository.existsBySlug(any()) } returns true

        val exception = assertThrows<DomainException> {
            projectService.create(request, userId)
        }

        assertEquals(ErrorType.PROJECT_ALREADY_EXISTS, exception.type)
        verify(exactly = 1) { userService.findById(userId) }
        verify(exactly = 1) { projectRepository.existsBySlug(any()) }
        verify(exactly = 0) { projectRepository.save(any()) }
        verify(exactly = 0) { metricsUtils.counter(any(), any(), any()) }
    }

    @Test
    fun `given a user that does not exist when creating then throw DomainException`() {
        val userId = "user-1"
        val request = ProjectDto.Create(name = "Project 1", description = "Description 1")

        every { userService.findById(userId) } throws DomainException(ErrorType.USER_NOT_FOUND)

        val exception = assertThrows<DomainException> {
            projectService.create(request, userId)
        }

        assertEquals(ErrorType.USER_NOT_FOUND, exception.type)
        verify(exactly = 1) { userService.findById(userId) }
        verify(exactly = 0) { projectRepository.existsBySlug(any()) }
        verify(exactly = 0) { projectRepository.save(any()) }
    }
}
