package com.kaiqkt.nerv.domain.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64

@Service
class CryptoService(
    @Value($$"${security.token-secret}")
    private val secret: String,
) {
    private val key = SecretKeySpec(secret.toByteArray(), "AES")

    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return Base64.encode(cipher.doFinal(value.toByteArray()))
    }

    fun decrypt(value: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        return String(cipher.doFinal(Base64.decode(value)))
    }
}
