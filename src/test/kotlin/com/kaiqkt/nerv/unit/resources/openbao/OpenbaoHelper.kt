package com.kaiqkt.nerv.unit.resources.openbao

import com.kaiqkt.nerv.resources.openbao.requests.SecretEngineRequest
import com.kaiqkt.nerv.resources.openbao.responses.NamespaceDataResponse
import com.kaiqkt.nerv.unit.resources.utils.MockServerHolder
import com.kaiqkt.nerv.resources.openbao.responses.NamespaceResponse
import io.restassured.http.Method
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.http.HttpMethod

object OpenbaoHelper : MockServerHolder() {
    override fun domainPath(): String = "/openbao"

    fun mockCreateNamespaceSuccessfully(namespace: String) {
        mockServer()
            .`when`(
                HttpRequest
                    .request()
                    .withMethod(HttpMethod.POST.name())
                    .withPath("${domainPath()}/v1/sys/namespaces/$namespace"),
            ).respond(
                HttpResponse
                    .response()
                    .withStatusCode(200),
            )
    }

    fun mockListNamespacesSuccessfully(keys: List<String>) {
        val response = NamespaceResponse(data = NamespaceDataResponse(keys))

        mockServer()
            .`when`(
                HttpRequest
                    .request()
                    .withMethod(HttpMethod.GET.name())
                    .withPath("${domainPath()}/v1/sys/namespaces")
                    .withQueryStringParameter("list", "true"),
            ).respond(
                HttpResponse
                    .response()
                    .withBody(objectMapper.writeValueAsString(response))
                    .withStatusCode(200),
            )
    }

    fun mockEnableSecretEngineSuccessfully(
        mountPath: String,
        engineSecretType: String,
        namespace: String,
    ) {
        val request = SecretEngineRequest(type = engineSecretType)

        mockServer()
            .`when`(
                HttpRequest
                    .request()
                    .withMethod(HttpMethod.POST.name())
                    .withPath("${domainPath()}/v1/sys/mounts/$mountPath")
                    .withBody(objectMapper.writeValueAsString(request))
                    .withHeader("X-Vault-Namespace", namespace),
            ).respond(
                HttpResponse
                    .response()
                    .withStatusCode(200),
            )
    }

    fun mockCreateSecretSuccessfully(
        mountPath: String,
        key: String,
        namespace: String,
        request: Map<String, String>,
    ) {
        mockServer()
            .`when`(
                HttpRequest
                    .request()
                    .withMethod(HttpMethod.POST.name())
                    .withPath("${domainPath()}/v1/$mountPath/$key")
                    .withBody(objectMapper.writeValueAsString(request))
                    .withHeader("X-Vault-Namespace", namespace),
            ).respond(
                HttpResponse
                    .response()
                    .withStatusCode(200),
            )
    }

    fun verifyCreateNamespaceRequest(namespace: String) {
        verifyRequest(Method.POST, "${domainPath()}/v1/sys/namespaces/$namespace", 1)
    }

    fun verifyListNamespacesRequest() {
        verifyRequest(Method.GET, "${domainPath()}/v1/sys/namespaces", 1)
    }

    fun verifyEnableSecretEngineRequest(mountPath: String) {
        verifyRequest(Method.POST, "${domainPath()}/v1/sys/mounts/$mountPath", 1)
    }

    fun verifyCreateSecretRequest(
        mountPath: String,
        key: String,
        times: Int = 1
    ) {
        verifyRequest(Method.POST, "${domainPath()}/v1/$mountPath/$key", times)
    }
}
