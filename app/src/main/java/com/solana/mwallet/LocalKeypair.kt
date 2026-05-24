/*
 * Copyright (c) 2022 Solana Mobile Inc.
 */

package com.solana.mwallet

import android.util.Base64
import com.funkatronics.encoders.Base58
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters

object LocalKeypair {
    fun getPrivateKey(): ByteArray? =
        BuildConfig.PRIVATE_KEY?.let(::decodePrivateKey)

    fun getPublicKey(): String? =
        getPrivateKey()?.let { privateKey ->
            val privateKeyParams = Ed25519PrivateKeyParameters(privateKey, 0)
            Base58.encodeToString(privateKeyParams.generatePublicKey().encoded)
        }

    private fun decodePrivateKey(privateKey: String): ByteArray =
        try {
            Base58.decode(privateKey)
        } catch (_: Throwable) {
            try {
                val standardBase64NoPadding = privateKey.replace("-", "+").replace("_", "/").trimEnd('=')
                Base64.decode(standardBase64NoPadding, Base64.NO_PADDING or Base64.NO_WRAP)
            } catch (_: IllegalArgumentException) {
                throw IllegalArgumentException("could not decode provided private key from local props")
            }
        }
}
