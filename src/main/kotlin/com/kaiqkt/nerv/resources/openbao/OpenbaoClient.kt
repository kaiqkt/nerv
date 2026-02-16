package com.kaiqkt.nerv.resources.openbao

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.isSuccessful
import com.kaiqkt.nerv.resources.exceptions.UnexpectedResourceException
import com.kaiqkt.nerv.resources.openbao.requests.SecretEngineRequest
import com.kaiqkt.nerv.resources.openbao.responses.NamespaceDataResponse
import com.kaiqkt.nerv.utils.MetricsUtils
import com.kaiqkt.nerv.resources.openbao.responses.NamespaceResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class OpenbaoClient(
    private val mapper: ObjectMapper,
    private val metricsUtils: MetricsUtils,
    private val properties: OpenbaoProperties
) {
    fun createNamespace(namespace: String) {
        val (_, response, _) =
            metricsUtils.request("openbao_create_namespace") {
                Fuel
                    .post("${properties.apiUrl}/v1/sys/namespaces/$namespace")
                    .header("X-Vault-Token" to properties.apiKey)
                    .response()
            }


        if (response.isSuccessful) {
            return
        }

        throw UnexpectedResourceException("Error trying to create a openbao vault namespace")
    }

    fun enableSecretEngine(namespace: String) {
        val request = SecretEngineRequest(properties.secretEngine.type)

        val (_, response, _) =
            metricsUtils.request("openbao_enable_secret_engine") {
                Fuel
                    .post("${properties.apiUrl}/v1/sys/mounts/${properties.secretEngine.mountPath}")
                    .header("X-Vault-Token" to properties.apiKey)
                    .header("X-Vault-Namespace" to namespace)
                    .body(mapper.writeValueAsString(request))
                    .response()
            }

        if (response.isSuccessful) {
            return
        }

        throw UnexpectedResourceException(response.responseMessage)
    }

    fun listNamespaces(): NamespaceResponse {
        val (_, response, result) =
            metricsUtils.request("openbao_find_namespace") {
                Fuel
                    .get("${properties.apiUrl}/v1/sys/namespaces", listOf("list" to true))
                    .header("X-Vault-Token" to properties.apiKey)
                    .response()
            }

        return when {
            response.isSuccessful -> mapper.readValue(result.get(), NamespaceResponse::class.java)
            response.statusCode == HttpStatus.NOT_FOUND.value() -> NamespaceResponse(NamespaceDataResponse(emptyList()))
            else -> throw UnexpectedResourceException(response.responseMessage)
        }
    }

    fun saveSecret(
        namespace: String,
        key: String,
        request: Map<String, String>,
    ) {
        val (_, response, _) = metricsUtils.request("openbao_save_secret") {
            Fuel
                .post("${properties.apiUrl}/v1/${properties.secretEngine.mountPath}/$key")
                .header("X-Vault-Token" to properties.apiKey)
                .header("X-Vault-Namespace" to namespace)
                .body(mapper.writeValueAsString(request))
                .response()
        }

        if (response.isSuccessful) {
            return
        }

        throw UnexpectedResourceException(response.responseMessage)
    }
}