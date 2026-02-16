package com.kaiqkt.nerv.resources.openbao

import com.kaiqkt.nerv.domain.gateways.VaultGateway
import org.springframework.stereotype.Component

@Component
class OpenbaoGatewayImpl(
    val openbaoClient: OpenbaoClient
) : VaultGateway {
    override fun createNamespace(name: String) {
        openbaoClient.createNamespace(name)
        openbaoClient.enableSecretEngine(name)
    }

    override fun listNamespaces(): List<String> {
        return openbaoClient.listNamespaces().data.keys.map { it.replace("/", "") }
    }

    override fun saveSecret(
        namespace: String,
        key: String,
        value: String,
    ) {
        openbaoClient.saveSecret(namespace, key, mapOf(key to value))
    }
}