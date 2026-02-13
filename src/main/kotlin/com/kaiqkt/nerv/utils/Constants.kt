package com.kaiqkt.nerv.utils

import org.slf4j.MDC
import java.util.UUID

object Constants {
    object Parameters {
        val requestId = MDC.get("request_id") ?: UUID.randomUUID().toString()
    }

    object Keys

    object Headers

    object Sort
}
