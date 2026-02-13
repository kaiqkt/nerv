package com.kaiqkt.nerv.application.exceptions

class InvalidRequestException(
    val errors: Map<String, Any>,
) : Exception()
