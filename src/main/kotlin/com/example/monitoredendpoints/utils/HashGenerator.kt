package com.example.monitoredendpoints.utils

import org.springframework.stereotype.*
import java.util.*
import javax.crypto.*
import javax.crypto.spec.*

@Component
class HashGenerator {

    fun generateHash(base: String): String {
        val salt = System.currentTimeMillis().toString()
        val keySpec = PBEKeySpec(base.toCharArray(), salt.toByteArray(), 65536, 128)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        return Base64.getUrlEncoder().encodeToString(factory.generateSecret(keySpec).encoded)
    }
}