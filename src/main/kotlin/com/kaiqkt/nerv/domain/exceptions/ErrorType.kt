package com.kaiqkt.nerv.domain.exceptions

enum class ErrorType(
    val message: String,
) {
    VAULT_ALREADY_EXISTS("Vault already exists"),
    PROJECT_ALREADY_EXISTS("Project already exists")
}
