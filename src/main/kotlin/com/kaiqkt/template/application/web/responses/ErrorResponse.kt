package ${package}.application.web.responses

data class ErrorResponse(
    val type: String,
    val message: String?,
    val details: Map<String, Any>,
)
