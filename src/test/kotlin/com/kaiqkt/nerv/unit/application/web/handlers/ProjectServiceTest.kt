package com.kaiqkt.nerv.unit.application.web.handlers

import com.kaiqkt.nerv.domain.repositories.ProjectRepository
import com.kaiqkt.nerv.utils.MetricsUtils
import io.mockk.mockk

class ProjectServiceTest {
    val projectRepository: ProjectRepository = mockk(relaxed = true)
    val metricsUtils: MetricsUtils = mockk(relaxed = true)
}