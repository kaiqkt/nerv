package com.kaiqkt.nerv.domain.services

import com.kaiqkt.nerv.domain.gateways.VaultGateway
import com.kaiqkt.nerv.domain.results.CreateVaultResult
import com.kaiqkt.nerv.utils.Constants.Metrics.STATUS
import com.kaiqkt.nerv.utils.Constants.Metrics.CREATED
import com.kaiqkt.nerv.utils.Constants.Metrics.ALREADY_EXISTS
import com.kaiqkt.nerv.utils.MetricsUtils
import org.springframework.stereotype.Service

@Service
class VaultService(
    private val vaultGateway: VaultGateway,
    private val metricsUtils: MetricsUtils
) {
    fun create(name: String): CreateVaultResult {
        val namespaces = vaultGateway.listNamespaces()

        if (namespaces.contains(name)) {
            metricsUtils.counter("vault", STATUS, ALREADY_EXISTS)

            return CreateVaultResult.AlreadyExists
        }

        vaultGateway.createNamespace(name)
        metricsUtils.counter("vault", STATUS, CREATED)

        return CreateVaultResult.Created
    }
}