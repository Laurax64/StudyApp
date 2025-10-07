package com.example.studyapp.ui.authentication

import java.security.SecureRandom
import java.util.Base64

object UserIdGenerator {
    private val secureRandom = SecureRandom()

    /**
     * Generates a 16-byte random user ID and returns it as URL-safe Base64 without padding.
     * Example output: "q0x2F1jK9s4tZ6v8YpQ3_A"
     */
    fun generateUserIdBase64(): String {
        val bytes = ByteArray(16)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    /**
     * Generates a 16-byte random user ID and returns it as a lowercase hex string.
     * Example output: "3a7f1c2b9e4d6a8f0b1c2d3e4f5a6b7c"
     */
    fun generateUserIdHex(): String {
        val bytes = ByteArray(16)
        secureRandom.nextBytes(bytes)
        return bytes.joinToString(separator = "") { "%02x".format(it) }
    }

    /**
     * Convenience: returns a UUID-like representation by using the 16 bytes as UUID fields.
     * NOTE: This is just a formatting option; it is NOT a real time-based UUID.
     * Example output: "3a7f1c2b-9e4d-6a8f-0b1c-2d3e4f5a6b7c"
     */
    fun generateUserIdDashedHex(): String {
        val hex = generateUserIdHex()
        return listOf(
            hex.substring(0, 8),
            hex.substring(8, 12),
            hex.substring(12, 16),
            hex.substring(16, 20),
            hex.substring(20, 32)
        ).joinToString("-")
    }
}
