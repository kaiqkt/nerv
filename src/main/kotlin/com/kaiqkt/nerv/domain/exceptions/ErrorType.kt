package com.kaiqkt.nerv.domain.exceptions

enum class ErrorType(
    val message: String,
) {
    PROJECT_ALREADY_EXISTS("Project already exists"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    NICKNAME_ALREADY_EXISTS("Nickname already exists"),
    USER_NOT_FOUND("User not found"),
    GIT_ACCESS_TOKEN_ALREADY_EXISTS("Git Access Token already exists"),
}
