package com.kaiqkt.nerv.domain.results

sealed class CreateVaultResult{
    object Created: CreateVaultResult()
    object AlreadyExists: CreateVaultResult()
}