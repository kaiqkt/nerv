package com.kaiqkt.nerv.application.web.requests

import com.kaiqkt.nerv.domain.models.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

sealed class UserRequest {
    data class Create(
        @field:NotBlank(message = "input should not be blank")
        @field:Size(message = "must not exceed 255 characters", max = 255)
        val name: String,
        @field:NotBlank(message = "input should not be blank")
        @field:Size(message = "must not exceed 25 characters", max = 25)
        val nickname: String,
        @field:NotBlank(message = "input should not be blank")
        @field:Email(message = "must be a valid email")
        @field:Size(message = "must not exceed 255 characters", max = 255)
        val email: String,
        @field:Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$",
            message = "must be at least 8 characters long and include at least one letter, one special character, and one number",
        )
        @field:Size(message = "must not exceed 255 characters", max = 255)
        val password: String,
    ) : ProjectRequest()
}

fun UserRequest.Create.toDomain(): User =
    User(
        name = this.name,
        nickname = this.nickname,
        email = this.email,
        password = this.password,
    )
