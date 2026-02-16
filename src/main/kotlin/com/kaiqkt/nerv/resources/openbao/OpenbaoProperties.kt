package com.kaiqkt.nerv.resources.openbao

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "openbao")
data class OpenbaoProperties(
    val apiUrl: String,
    val apiKey: String,
    val secretEngine: OpenbaoSecretEngineProperties
)

data class OpenbaoSecretEngineProperties(
    val type: String,
    val mountPath: String
)