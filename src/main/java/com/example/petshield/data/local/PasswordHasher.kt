package com.example.petshield.data.local

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

internal data class PasswordDigest(val salt: String, val hash: String)

internal object PasswordHasher {
    private const val ITERATIONS = 120_000
    private const val KEY_LENGTH_BITS = 256
    private const val SALT_LENGTH_BYTES = 16

    fun create(password: String): PasswordDigest {
        val saltBytes = ByteArray(SALT_LENGTH_BYTES).also(SecureRandom()::nextBytes)
        return PasswordDigest(
            salt = Base64.encodeToString(saltBytes, Base64.NO_WRAP),
            hash = derive(password, saltBytes)
        )
    }

    fun verify(password: String, salt: String, expectedHash: String): Boolean = runCatching {
        val saltBytes = Base64.decode(salt, Base64.NO_WRAP)
        val expectedBytes = Base64.decode(expectedHash, Base64.NO_WRAP)
        val actualBytes = Base64.decode(derive(password, saltBytes), Base64.NO_WRAP)
        MessageDigest.isEqual(expectedBytes, actualBytes)
    }.getOrDefault(false)

    private fun derive(password: String, salt: ByteArray): String {
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS)
        return try {
            val derived = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec)
                .encoded
            Base64.encodeToString(derived, Base64.NO_WRAP)
        } finally {
            spec.clearPassword()
        }
    }
}