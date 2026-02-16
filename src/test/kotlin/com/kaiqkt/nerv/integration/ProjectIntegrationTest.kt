package com.kaiqkt.nerv.integration

import com.kaiqkt.nerv.application.web.requests.ProjectRequest
import com.kaiqkt.nerv.application.web.responses.ErrorResponse
import com.kaiqkt.nerv.application.web.responses.ProjectResponse
import com.kaiqkt.nerv.domain.models.Project
import com.kaiqkt.nerv.unit.resources.openbao.OpenbaoHelper
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class ProjectIntegrationTest : IntegrationTest() {
    @Test
    fun `given a request to create a project when the request is invalid should thrown an error`() {
        val request = ProjectRequest.Create(
            name = "",
            description = "description".repeat(255)
        )

        val response = given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/v1/projects")
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .extract()
            .`as`(ErrorResponse::class.java)

        assertEquals("must contain 1 to 25 characters", response.details["name"])
        assertEquals("must not exceed 255 characters", response.details["description"])
    }

    @Test
    fun `given a request to create a project when already exist a project with same name should return an error`() {
        Project(
            name = "initial project",
            description = "description"
        ).also(projectRepository::save)

        val request = ProjectRequest.Create(
            name = "Initial Project",
            description = "description"
        )

        val response = given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/v1/projects")
            .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .extract()
            .`as`(ErrorResponse::class.java)

        assertEquals("Project already exists", response.message)
    }

    @Test
    fun `given a request to create a project when a vault already exists with same name should thrown an error`() {
        val request = ProjectRequest.Create(
            name = "Initial Project",
            description = "description"
        )

        OpenbaoHelper.mockListNamespacesSuccessfully(listOf("initial-project"))

        val response = given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/v1/projects")
            .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .extract()
            .`as`(ErrorResponse::class.java)

        assertEquals("Vault already exists", response.message)

        OpenbaoHelper.verifyListNamespacesRequest()
    }

    @Test
    fun `given a request a create a project should create successfully`() {
        val request = ProjectRequest.Create(
            name = "Initial Project",
            description = "description"
        )

        OpenbaoHelper.mockListNamespacesSuccessfully(listOf())
        OpenbaoHelper.mockCreateNamespaceSuccessfully("initial-project")
        OpenbaoHelper.mockEnableSecretEngineSuccessfully("secrets", "kv", "initial-project")

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/v1/projects")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .`as`(ProjectResponse::class.java)

        OpenbaoHelper.verifyListNamespacesRequest()
        OpenbaoHelper.verifyCreateNamespaceRequest("initial-project")
        OpenbaoHelper.verifyEnableSecretEngineRequest("secrets")
    }

}