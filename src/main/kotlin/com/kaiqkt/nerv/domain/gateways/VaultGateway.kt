package com.kaiqkt.nerv.domain.gateways

interface VaultGateway {
    fun createNamespace(name: String)
    fun listNamespaces(): List<String>
    fun saveSecret(
        namespace: String,
        key: String,
        value: String,
    )
}
