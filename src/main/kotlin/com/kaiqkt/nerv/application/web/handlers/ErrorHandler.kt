package com.kaiqkt.nerv.application.web.handlers

import com.kaiqkt.nerv.application.exceptions.InvalidRequestException
import com.kaiqkt.nerv.application.web.responses.ErrorResponse
import com.kaiqkt.nerv.domain.exceptions.DomainException
import com.kaiqkt.nerv.domain.exceptions.ErrorType
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ErrorHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(ex.type.name, ex.message ?: "error", mapOf())

        return ResponseEntity(error, getStatusCode(ex.type))
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(ex: MissingRequestHeaderException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse("MISSING_HEADER", "Missing header", mapOf(ex.headerName to "required header"))

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(InvalidRequestException::class)
    fun handleInvalidRequestException(ex: InvalidRequestException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse("INVALID_REQUEST", "Invalid request", ex.errors)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    public override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any> {
        val details = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "invalid") }

        val error = ErrorResponse("INVALID_REQUEST_ARGUMENTS", "Invalid method arguments", details)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val details =
            ex.constraintViolations.associate { v ->
                val path = v.propertyPath.joinToString(".") { it.name }
                path to v.message
            }

        val error = ErrorResponse("CONSTRAINT_VIOLATION", "Constraint violation", details)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    private fun getStatusCode(type: ErrorType): HttpStatus =
        when (type) {
            ErrorType.PROJECT_ALREADY_EXISTS -> HttpStatus.CONFLICT
            ErrorType.EMAIL_ALREADY_EXISTS -> HttpStatus.CONFLICT
            ErrorType.NICKNAME_ALREADY_EXISTS -> HttpStatus.CONFLICT
            ErrorType.USER_NOT_FOUND -> HttpStatus.NOT_FOUND
            ErrorType.GIT_ACCESS_TOKEN_ALREADY_EXISTS -> HttpStatus.CONFLICT
            ErrorType.INVALID_TOKEN -> HttpStatus.BAD_REQUEST
            ErrorType.EXPIRED_TOKEN -> HttpStatus.UNAUTHORIZED
        }
}
