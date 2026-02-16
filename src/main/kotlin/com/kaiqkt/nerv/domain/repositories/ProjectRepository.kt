package com.kaiqkt.nerv.domain.repositories

import com.kaiqkt.nerv.domain.models.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository: JpaRepository<Project, String> {
    fun existsBySlug(name: String): Boolean
}