package com.kaiqkt.nerv.application.exceptions

class UnauthorizedException(
    override val message: String,
    val type: String,
) : Exception()
