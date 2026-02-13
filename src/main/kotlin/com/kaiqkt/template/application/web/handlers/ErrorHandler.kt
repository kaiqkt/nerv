package ${package}.application.web.handlers

import ${package}.application.exceptions.InvalidRequestException
import ${package}.application.web.responses.ErrorResponse
import ${package}.domain.exceptions.DomainException
import ${package}.domain.exceptions.ErrorType
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
    companion object {
        private const val INVALID_REQUEST = "INVALID_REQUEST"
        private const val INVALID_REQUEST_MESSAGE = "Invalid request"
    }

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(ex.type.name, ex.message, mapOf())

        return ResponseEntity(error, getStatusCode(ex.type))
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(ex: MissingRequestHeaderException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(INVALID_REQUEST, INVALID_REQUEST_MESSAGE, mapOf(ex.headerName to "required header"))

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(InvalidRequestException::class)
    fun handleInvalidRequestException(ex: InvalidRequestException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(INVALID_REQUEST, INVALID_REQUEST_MESSAGE, ex.errors)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    public override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any> {
        val details = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "invalid") }

        val error = ErrorResponse(INVALID_REQUEST, INVALID_REQUEST_MESSAGE, details)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val details =
            ex.constraintViolations.associate { v ->
                val path = v.propertyPath.joinToString(".") { it.name }
                path to v.message
            }

        val error = ErrorResponse(INVALID_REQUEST, INVALID_REQUEST_MESSAGE, details)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    private fun getStatusCode(type: ErrorType): HttpStatus = HttpStatus.NOT_IMPLEMENTED

}
