package com.insideout.usecase.member.port

import javax.crypto.SecretKey

interface TokenPort {
    fun create(
        subject: String,
        secretKey: SecretKey,
        expirationMillis: Long,
    ): String

    fun <T> parse(
        token: String,
        secretKey: SecretKey,
        converter: (String) -> T,
    ): T
}
